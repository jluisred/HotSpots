package fr.eurecom.hotspots.extraction;



import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;


import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import fr.eurecom.hotspots.datastructures.TRTopic;
import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;



public class TextRazor 
{
	private static String SOURCE = "TextRazor";
	private static String ENDPOINT = "http://api.textrazor.com/";

	
	private String key = "";
    public TextRazor (String key){
    	this.key = key;
    }
    public TextRazor (){
    	this.key = "d79480bfd0b17e8cff686532f44ab7ea9d9eac0d98e531c94a0ea1ba";
    }
    public static void main(String[] args) 
    {
        String key = "d79480bfd0b17e8cff686532f44ab7ea9d9eac0d98e531c94a0ea1ba";
        String text = "Spain's stricken Bankia expects to sell off its vast portfolio of industrial holdings that includes a stake in the parent company of British Airways and Iberia.";

        TextRazor c = new TextRazor(key);
        c.extractTopic(text);
    }

    public ArrayList<NERDEntity> extractEntity(String document) 
    {
    	
    	
        System.out.println(SOURCE + " is going to extract entities from a document");
        
        String json = postEntity(key, document);
        

        ArrayList<NERDEntity> result = parseEntities (json, document);
      

        System.out.println(SOURCE + " has found #entities=" + result.size());
        return result;
    }
    
    

	public ArrayList<TRTopic> extractTopic(String document) {
        System.out.println(SOURCE + " is going to extract topic from a document");
        
        String json = postTopic(key, document);
        

        ArrayList<TRTopic> result = parseTopics (json, document);
      

        System.out.println(SOURCE + " has found #topics=" + result.size());
        return result;
	}
	
	
	
    private ArrayList<NERDEntity> parseEntities(String json, String document) 
    {
    	ArrayList<NERDEntity> result = new ArrayList<NERDEntity>();
        Gson gson = new Gson();
        
        try {
            JSONObject o = new JSONObject(json);
            JSONObject response = o.getJSONObject("response");
            String entityjson = response.getJSONArray("entities").toString();

            //System.out.println(entityjson);
            Type listType = new TypeToken<ArrayList<TextRazorEntity>>() {}.getType();
            List<TextRazorEntity>  entities = gson.fromJson(entityjson, listType); 
            for(TextRazorEntity e : entities) 
            {
                Integer startChar = e.getStartingPos();
                Integer endChar = e.getEndingPos();
                
                //String label = document.getText().substring(startChar, endChar);
                String label = e.getMatchedText();
                
                // we relax a bit this constraint
                //if( ! label.equalsIgnoreCase(e.getMatchedText()) )
                //    continue;

                String uri = e.getWikiLink();
                String type = "";
                Boolean found = false;
                if(e.getType()!=null) {
                	type="DBpedia:";
                    for(String t : e.getType())
                    	type += t.concat(",");
                    type = type.substring(0, type.length()-1);
                    found = true;
                }
                if (e.getFreebaseTypes() != null){
                    if (found) type +=";";
                    type += "Freebase:";
                    for(String t : e.getFreebaseTypes())
                    	type += t.concat(",");
                    type = type.substring(0, type.length()-1);
                    found = true;
                }
                if(!found) type = "null";
                
                
                
                String nerdType = "http://nerd.eurecom.fr/ontology#Thing";
                if (type != null) {
                  if ((type.indexOf("Person") != -1) || (type.indexOf("/people/person") != -1))
                    nerdType = "http://nerd.eurecom.fr/ontology#Person";
                  else if ((type.indexOf("Place") != -1) || 
                    (type.indexOf("AdministrativeRegion") != -1) || 
                    (type.indexOf("AdministrativeArea") != -1) || 
                    (type.indexOf("Settlement") != -1) || 
                    (type.indexOf("/location/statistical_region") != -1) || 
                    (type.indexOf("/location/citytown") != -1) || 
                    (type.indexOf("/location/location") != -1))
                    nerdType = "http://nerd.eurecom.fr/ontology#Location";
                  else if ((type.indexOf("Organization") != -1) || (type.indexOf("Organisation") != -1) || (type.indexOf("/organization/organization") != -1)) {
                    nerdType = "http://nerd.eurecom.fr/ontology#Organization";
                  }
                }
                
                
                Double confidence = e.getConfidenceScore();
                Double relevance = e.getRelevanceScore();
                

                //PATCH
                if(uri != null){
                    if(relevance == 0.0 && uri.equals("")){
                    	relevance = 0.5;
                    }
                }

                
                NERDEntity entityI = new NERDEntity(label,
                                             startChar,
                                             endChar,
                                             type,
                                             nerdType,
                                             uri,
                                             confidence, 
                                             relevance
                                            );

                //System.out.println(entity.toString());
                result.add(entityI);
                
            }
            
        } catch (JSONException e) {
        	System.out.println(json);
            e.printStackTrace();
        }  
        return result;
    }

    private ArrayList<TRTopic> parseTopics(String json, String document) 
    {
    	ArrayList<TRTopic> result = new ArrayList<TRTopic>();
        Gson gson = new Gson();
        
        try {
            JSONObject o = new JSONObject(json);
            JSONObject response = o.getJSONObject("response");
            String topicjson = response.getJSONArray("topics").toString();

            //System.out.println(entityjson);
            Type listType = new TypeToken<ArrayList<TextRazorTopic>>() {}.getType();
            List<TextRazorTopic>  topics = gson.fromJson(topicjson, listType); 
            
            for(TextRazorTopic t : topics) 
            {

                //String label = document.getText().substring(startChar, endChar);
                String label = t.getLabel();
                
                // we relax a bit this constraint
                //if( ! label.equalsIgnoreCase(e.getMatchedText()) )
                //    continue;

                String uri = t.getWikiLink();

                		
                Double score = t.getScore();
                          
                TRTopic topicI = new TRTopic(label, uri, score);

                //System.out.println(entity.toString());
                result.add(topicI);
                
            }
            
        } catch (JSONException e) {
        	System.out.println(json);
            e.printStackTrace();
        }  
        return result;
    }
    
    
    
    
    private String postEntity(String key, String document) 
    {
        // CLI
        // curl -i -X POST -d "apiKey=d79480bfd0b17e8cff686532f44ab7ea9d9eac0d98e531c94a0ea1ba" -d "extractors=entities" -d "text=Spain's stricken Bankia expects to sell off its vast portfolio of industrial holdings that includes a stake in the parent company of British Airways and Iberia." http://api.textrazor.com/
        
        Client client = Client.create();        
        WebResource webResource = client.resource(ENDPOINT);
        
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("apiKey", key);
        params.add("extractors", "entities");
        params.add("text", document);
                
        ClientResponse response = webResource
                                    .accept(MediaType.APPLICATION_JSON_TYPE)
                                    .post(ClientResponse.class, params);
        
        if( response.getStatus() != 200 )
            System.out.println("Extractor: " + SOURCE + " is temporary not available. " +  response.getStatus() + "   "+response.getEntity(String.class));
        
        String json = response.getEntity(String.class);
        return json;
    }
    
    
    private String postTopic(String key, String document) 
    {

        Client client = Client.create();        
        WebResource webResource = client.resource(ENDPOINT);
        
        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("apiKey", key);
        params.add("extractors", "topics");
        params.add("text", document);
                
        ClientResponse response = webResource
                                    .accept(MediaType.APPLICATION_JSON_TYPE)
                                    .post(ClientResponse.class, params);
        
        if( response.getStatus() != 200 )
            System.out.println("Extractor: " + SOURCE + " is temporary not available. " +  response.getStatus() + "   "+response.getEntity(String.class));
        
        String json = response.getEntity(String.class);
        return json;
    }
    
    
    
    class TextRazorEntity {
        private Integer id;
        private List<String> type;
        private List<Integer> matchingTokens;
        private String entityId;
        private List<String> freebaseTypes;
        private Double confidenceScore;
        private String wikiLink;
        private String matchedText;
        private String freebaseId;
        private Double relevanceScore;
        private String entityEnglishId;
        private Integer startingPos;
        private Integer endingPos;
        
        public Integer getId() {
            return id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
        
        public List<String> getType() {
            return type;
        }
        public void setType(List<String> type) {
            this.type = type;
        }
        public List<Integer> getMatchingTokens() {
            return matchingTokens;
        }
        public void setMatchingTokens(List<Integer> matchingTokens) {
            this.matchingTokens = matchingTokens;
        }
        public String getEntityId() {
            return entityId;
        }
        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }
        public List<String> getFreebaseTypes() {
            return freebaseTypes;
        }
        public void setFreebaseTypes(List<String> freebaseTypes) {
            this.freebaseTypes = freebaseTypes;
        }
        public Double getConfidenceScore() {
            return confidenceScore;
        }
        public void setConfidenceScore(Double confidenceScore) {
            this.confidenceScore = confidenceScore;
        }
        public String getWikiLink() {
            return wikiLink;
        }
        public void setWikiLink(String wikiLink) {
            this.wikiLink = wikiLink;
        }
        public String getMatchedText() {
            return matchedText;
        }
        public void setMatchedText(String matchedText) {
            this.matchedText = matchedText;
        }
        public String getFreebaseId() {
            return freebaseId;
        }
        public void setFreebaseId(String freebaseId) {
            this.freebaseId = freebaseId;
        }
        public Double getRelevanceScore() {
            return relevanceScore;
        }
        public void setRelevanceScore(Double relevanceScore) {
            this.relevanceScore = relevanceScore;
        }
        public String getEntityEnglishId() {
            return entityEnglishId;
        }
        public void setEntityEnglishId(String entityEnglishId) {
            this.entityEnglishId = entityEnglishId;
        }
        public Integer getStartingPos() {
            return startingPos;
        }
        public void setStartingPos(Integer startingPos) {
            this.startingPos = startingPos;
        }
        public Integer getEndingPos() {
            return endingPos;
        }
        public void setEndingPos(Integer endingPos) {
            this.endingPos = endingPos;
        }      
    }


    
    class TextRazorTopic {
        private Integer id;
        private String label;
        private String wikiLink;
        
        
        private Double score;
        private Integer startingPos;
        private Integer endingPos;
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getWikiLink() {
			return wikiLink;
		}
		public void setWikiLink(String wikiLink) {
			this.wikiLink = wikiLink;
		}
		public Double getScore() {
			return score;
		}
		public void setScore(Double score) {
			this.score = score;
		}
		public Integer getStartingPos() {
			return startingPos;
		}
		public void setStartingPos(Integer startingPos) {
			this.startingPos = startingPos;
		}
		public Integer getEndingPos() {
			return endingPos;
		}
		public void setEndingPos(Integer endingPos) {
			this.endingPos = endingPos;
		}
        
        
        
    }
}