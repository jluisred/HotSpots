package fr.eurecom.tvrdfizator.core.readers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.eurecom.tvrdfizator.core.datastructures.ItemLayer;
import fr.eurecom.tvrdfizator.core.datastructures.Layer;
import fr.eurecom.tvrdfizator.core.datastructures.Speaker;
import fr.eurecom.tvrdfizator.core.datastructures.Version;
import fr.eurecom.tvrdfizator.core.datastructures.VideoMetaData;


public class ExmaraldaReader{
	private File file;
	private VideoMetaData mdata; 
	private Document doc;
	
	private Hashtable  <Integer, String> times = new Hashtable <Integer, String>();
	private String videoURL;
	
	
	public ExmaraldaReader(File f, VideoMetaData md) throws ParserConfigurationException, SAXException, IOException{
		file = f;
		mdata = md;


			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();

			//String texto = FileUtils.readFileToString(fXmlFile);
			//System.out.println(texto.substring(0, 20));
			
			
			doc = dBuilder.parse(file);
			//doc = dBuilder.parse(fXmlFile);
			
			
			doc.getDocumentElement().normalize();
			


		
	}
	
	public void read(){
		
		//Get the URL of the video.
		
		Element n = (Element) doc.getElementsByTagName("referenced-file").item(0);
		videoURL = n.getAttribute("url");
		
		this.mdata.setVideoURL(videoURL);
		readSpeakers();
		readTimeline();
		readLayers();

		
	}


	private void readSpeakers(){
		
		//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("speaker");

		for (int temp = 0; temp < nList.getLength(); temp++) {

		   Node nNode = nList.item(temp);
		   Speaker spk = new Speaker();
		   
		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {

		      Element eElement = (Element) nNode;
		      
		      spk.setId(eElement.getAttribute("id"));
		      spk.setAbbreviation(getTagValue("abbreviation", eElement));
		      spk.setSex(getAttrValue("sex","value", eElement));	
		      if (getTagValue("languages-used", eElement) != null){
		    	  spk.addLanguage(getTagValue("languages-used", eElement) );
		      }
		      if (getTagValue("comment", eElement) != null){
		    	  spk.setComment(getTagValue("comment", eElement) );
		      }
		     eElement = (Element) eElement.getElementsByTagName("ud-speaker-information").item(0);
		     if (eElement.hasChildNodes()){
				 spk.setName(getTagValue("ud-information", eElement));	      
		     }
		   }
		   
		   mdata.addSpeaker(spk);
		}
		
		


		
	}
	
	
	
	private void readTimeline() {
		// TODO Auto-generated method stub
		//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("tli");

		for (int temp = 0; temp < nList.getLength(); temp++) {

		   Node nNode = nList.item(temp);
		   
		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {

		      Element eElement = (Element) nNode;
		      String id = eElement.getAttribute("id").substring(1);
		      String time = eElement.getAttribute("time");
		      
		     		      
		      times.put(Integer.parseInt(id), time); //Remove hundredth for Media Fragments spec.
		   }
		}
		
	}


	private void readLayers() {
		// TODO Auto-generated method stub
		//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("tier");

		for (int temp = 0; temp < nList.getLength(); temp++) {

		   Node nNode = nList.item(temp);
		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {

		      Element eElement = (Element) nNode;
		      Layer l = readLayer(eElement);
		      mdata.addLayer(l);
		   }
		}
		
	}
	
	
	private Layer readLayer(Element eElement) {
		// TODO Auto-generated method stub
		Layer l = new Layer ();
		
		//Read General Atributes.
	    l.setId(eElement.getAttribute("id"));
	    l.setSpeaker(eElement.getAttribute("speaker"));
	    l.setType(eElement.getAttribute("type"));
	    l.setCategory(eElement.getAttribute("category"));
	    l.setName(eElement.getAttribute("display-name"));

		
		//Read ud-tier Information
	    Element ud_Element = (Element) eElement.getElementsByTagName("ud-tier-information").item(0);
	     if (ud_Element.hasChildNodes()){
			 l.setUd_information(getTagValue("ud-information", ud_Element));	
			 l.setUd_information_name(getAttrValue("ud-information", "attribute-name", ud_Element));	
	     }
	     
		 //Read the Intervals
	     NodeList nListEvents = eElement.getElementsByTagName("event");
	     for (int temp = 0; temp < nListEvents.getLength(); temp++) {
			 Node nNode = nListEvents.item(temp);
			   
			 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				 ItemLayer mf = new ItemLayer();
				 Element eventElement = (Element) nNode;
				   
				 String startId = eventElement.getAttribute("start").substring(1);
				 String endId = eventElement.getAttribute("end").substring(1);
				 String start = times.get(Integer.parseInt(startId));
				 String end = times.get(Integer.parseInt(endId));
				 
				 mf.setEnd( Float.parseFloat(end));
				 mf.setStart(Float.parseFloat(start));
				 
				 
				 mf.setMediaFragmentURL("#t="+mf.getStart()+","+mf.getEnd());

				 
				 if (eventElement.getElementsByTagName("ud-information").getLength()>0){
					 for (int i = 0; i < eventElement.getElementsByTagName("ud-information").getLength(); i++){
						 
						 String ud_value = eventElement.getElementsByTagName("ud-information").item(i).getChildNodes().item(0).getNodeValue();
						 Element event_ud_element = (Element) eventElement.getElementsByTagName("ud-information").item(i);
						 String ud_name = event_ud_element.getAttribute("attribute-name");

						 mf.set_ud_information(ud_name, ud_value);
					 }					
				 }
				 if (eventElement.getChildNodes().getLength() > 0 ){
					NodeList nlList = eventElement.getChildNodes();
					Node nValue = (Node) nlList.item(eventElement.getChildNodes().getLength() -1);
					mf.setValue(nValue.getNodeValue());
				 }
			/*	 if (eventElement.getChildNodes().getLength() == 2){
					NodeList nlList = eventElement.getChildNodes();
					Node nValue = (Node) nlList.item(1);
					mf.setValue(nValue.getNodeValue());
					System.out.println("ALLIII");

				 }
			*/
			/*	 if(eventElement.getChildNodes().getLength() > 2){
						System.out.println("ESTA SI " + eventElement.getChildNodes().getLength());
						for(int i = 0; i <  eventElement.getChildNodes().getLength() ; i++){
							Node nodito = (Node) (eventElement.getChildNodes().item(i));
							System.out.println("{"+i+"}  "+nodito.getNodeName() + "   " + nodito.getNodeValue() + "   " + nodito.getNodeType());
						}
				 }*/
				 
				 l.addFragment(mf);
			 }
			 
	    	
	     }

		return l;
		
	}

	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	 
	        Node nValue = (Node) nlList.item(0);
	        if (nValue != null) return nValue.getNodeValue();
	        else return null;
	  }

	private static String getAttrValue(String sTag,String attr, Element eElement) {
		Node nValue = eElement.getElementsByTagName(sTag).item(0).getAttributes().getNamedItem(attr);
		return nValue.getNodeValue();
	}
	
}
