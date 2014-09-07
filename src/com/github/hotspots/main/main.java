package com.github.hotspots.main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl;
import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl.Line;
import com.googlecode.mp4parser.srt.SrtParser;

import fr.eurecom.hotspots.core.FragmentClustering;
import fr.eurecom.hotspots.core.LetterPairSimilarity;
import fr.eurecom.hotspots.core.Processing;
import fr.eurecom.hotspots.core.RankEntity;
import fr.eurecom.hotspots.core.RankFragment;
import fr.eurecom.hotspots.core.RankTopic;
import fr.eurecom.hotspots.datastructures.Fragment;
import fr.eurecom.hotspots.datastructures.TRTopic;
import fr.eurecom.hotspots.datastructures.Timeline;
import fr.eurecom.hotspots.datastructures.TopicComparator;
import fr.eurecom.hotspots.extraction.TextRazor;
import fr.eurecom.hotspots.serialization.HP_Result;
import fr.eurecom.hotspots.serialization.HotSpot;
import fr.eurecom.hotspots.srt.SRTMapper;
import fr.eurecom.tvrdfizator.core.datastructures.ItemLayer;
import fr.eurecom.tvrdfizator.core.datastructures.Layer;
import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;
import fr.eurecom.tvrdfizator.core.datastructures.VideoMetaData;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {


		//Download video
		
		//Upload video to Condat FTP.
		
		//Launch Processing in WP1 Service.
		
		//Get resutls from WP1 Service
		
		//Parse Shots and concepts from Exmeralda files.
		Timeline shots = parseExmeralda(new File("./WillPotter_2014U-950k/WillPotter_2014U-950k.exb"));
		
		//Parse Chapters from TED
		Timeline chapters = parseChapters(new File("./WillPotter_2014U-950k/WillPotter_2014U-950k.ch"));
		System.out.println("SH: " + shots.size() + "   //  CH: " + chapters.size());
		
		//Align subtitles to chapters:
		List<Line> subtitles = readSubtitles(new File("./WillPotter_2014U-950k/WillPotter_2014U-950k_sub.srt"));
		alignSubtitles(chapters, subtitles);
		
		System.out.println("=============================================================");
		
		//ANNOTATION
		//Get Topics
		//Align topics with chapters.
		
		//STRATEGY (1)
		/*	
		TextRazor extractorTopic = new TextRazor();
		ArrayList<TRTopic> topics = extractorTopic.extractTopic(convertSubToText(subtitles));
		Collections.sort(topics, new TopicComparator());
		for (TRTopic t : topics) 
			System.out.println(t.getLabel() + "    " + t.getRelevance());
		alignTopics(chapters, topics);*/
		
		//STRATEGY(2)
		Hashtable <String, Integer> alltopics = new Hashtable <String, Integer> ();
		for (Fragment chapter : chapters.getOrderedFragments()){
			TextRazor extractorTopic = new TextRazor();
			ArrayList<TRTopic> topics = extractorTopic.extractTopic(convertSubToText(chapter.getSubtitles()));
			try {
			    Thread.sleep(2000);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}			//System.out.println("**");
			if (topics.size() ==0 )  System.out.println(convertSubToText(chapter.getSubtitles()));
			Collections.sort(topics, new TopicComparator());
			for (TRTopic t : topics){
				chapter.getTopics().add(t);
				//System.out.println(t.getLabel() + "    " + t.getRelevance());
				
				if (alltopics.get(t.getLabel()) == null) alltopics.put(t.getLabel(), 1); 
				else alltopics.put(t.getLabel(), alltopics.get(t.getLabel())+1); 
			}
			//System.out.println("**");

		}
		//IDF
		for (Fragment chapter : chapters.getOrderedFragments()){
			for (TRTopic t : chapter.getTopics()){
				if (alltopics.contains(t.getLabel())){
					t.setInverseFrequency(alltopics.get(t.getLabel()));
				}
				else t.setInverseFrequency(0);
			}
		}
		//TF
		for (Fragment chapter : chapters.getOrderedFragments()){
			String textChapter = convertSubToText(chapter.getSubtitles());
			for (TRTopic t : chapter.getTopics()){
				if (StringUtils.containsIgnoreCase(textChapter, t.getLabel())){
					int frequencyTopic = appearancesText (textChapter, t.getLabel());
					t.setFrequency(frequencyTopic);
				}
			}
		}
		
		
		
		
		//Counting frequency
		
		System.out.println("=============================================================");

		
		//Get entities.
		//Align entities with chapters.
		TextRazor extractorEntity = new TextRazor();
		ArrayList<NERDEntity> entities = extractorEntity.extractEntity(convertSubToText(subtitles));
		SRTMapper mapper = new SRTMapper();
		mapper.run(subtitles, entities);
		System.out.println(entities.size());
		alignEntities(chapters, entities);
		//IDF
		for (Fragment ch : chapters.getOrderedFragments()){
			for (NERDEntity e : ch.getEntities()){
				int inverseFrequency = 0;
				ArrayList<NERDEntity> restEntities = new ArrayList <NERDEntity>();
				
				for (Fragment ch_other : chapters.getOrderedFragments()){
					if (!ch_other.equals(ch)) restEntities.addAll(ch_other.getEntities());
				}
				
				for (NERDEntity e_other : restEntities){
					if (LetterPairSimilarity.compareStrings(e_other.getLabel(), e.getLabel()) > 0.93){
						inverseFrequency++;
					}
				}
				e.setInverseFrequency(inverseFrequency);
			}
		}
		//TF (Coming) 



		
		System.out.println("=============================================================");

		//Merge shots and chapters
		Timeline visual_chapters = merge (chapters, shots);
		System.out.println("Visual CH: " + visual_chapters.size());
		
		System.out.println("=============================================================");

		//visual_chapters.plotSRT();

		//CLUSTERING		
		//Cluster similar chapters 
		FragmentClustering c = new FragmentClustering();
		c.cluster(visual_chapters);
		
		
		System.out.println("FINAL NUMBER OF CHAPTERS" + visual_chapters.size());
		
		
		System.out.println("=============================================================");

		//RANKING AND FILTERING
		//ranking entities and topics.
		rankAnnotations(visual_chapters);
		
		System.out.println("Annotations have been ranked.");
		System.out.println("=============================================================");

		//selecting. 
		System.out.println("Selecting main Chapters...");
		Timeline hotTopics = selectHotTopics(visual_chapters);
		System.out.println("The number of found Hot Spots is " + hotTopics.size());

		
		System.out.println("=============================================================");

		
		//Serialize
		int cont= 0;
		HP_Result results = new HP_Result();
		results.setUUID(UUID.randomUUID().toString());
		
		for (Fragment f : hotTopics.getOrderedFragments()){
			System.out.println("Chapter "+cont+":  "+f.getRelevance() + ", "+f.getStart() + ", "+f.getEnd());
			HotSpot hotSpot = new HotSpot();
			hotSpot.setStartNPT(f.getStart());
			hotSpot.setEndNPT(f.getEnd());

			//Entities and Topics
			double duration = f.getEnd() - f.getStart();
			int max_RelAnnotations = (int) Math.ceil(duration / (double)10);
			
			int max_Topics = max_RelAnnotations;
			if (f.getTopics().size() < max_RelAnnotations) max_Topics = f.getTopics().size();
				
			int max_Entities = max_RelAnnotations;
			if (f.getEntities().size() < max_RelAnnotations) max_Entities = f.getEntities().size();
				
			hotSpot.setTopic_list(f.getTopics().subList(0, max_Topics));
			hotSpot.setEntity_list(f.getEntities().subList(0, max_Entities));

			results.getHp_list().add(hotSpot);
			cont++;
		}
		
	    GsonBuilder gsonbuilder = new GsonBuilder();
	    Gson gson = gsonbuilder.create();
	    String itemsjson = gson.toJson(results);
	    
	    System.out.println(itemsjson);
	    try {
			FileUtils.writeStringToFile(new File("result.json"), itemsjson);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		System.out.println("END");

	}





	private static Timeline selectHotTopics(Timeline visual_chapters) {
		
		Timeline hotTopics = new Timeline();
		for (Fragment ch : visual_chapters.getOrderedFragments()){
			double fragmentScore = 0;
			double weightTopic = 0.4;
			double weightEntity = 0.6;
			
			double sumRelevanceTopics = 0;
			for (TRTopic t : ch.getTopics()){
				sumRelevanceTopics = sumRelevanceTopics+ t.getFinalScore();
			}
			
			double sumRelevanceEntities = 0;
			for (NERDEntity e : ch.getEntities()){
				sumRelevanceEntities = sumRelevanceEntities+ e.getFinalScore();
			}
			
			
			
			fragmentScore = (weightTopic*sumRelevanceTopics + weightEntity*sumRelevanceEntities) / (ch.getEnd() - ch.getStart());
			
			ch.setRelevance(fragmentScore);
			
		}

		//Filter and keep 1/4 more important
		int num_candidates = (int) Math.ceil((double)visual_chapters.getOrderedFragments().size()/(double)4);
		ArrayList <Fragment> hotSpot_candidates = new ArrayList <Fragment>();
		for (Fragment hotSpot_candidate : visual_chapters.getOrderedFragments()){
			hotSpot_candidates.add(hotSpot_candidate);
		}
		
		Collections.sort(hotSpot_candidates, new RankFragment());
		for (int i = 0; i <num_candidates; i++){
			hotTopics.insertFragment(hotSpot_candidates.get(i));
		}


		return hotTopics;
	}





	private static void rankAnnotations(Timeline visual_chapters) {
		
		for (Fragment chapter : visual_chapters.getOrderedFragments()){
			Collections.sort(chapter.getTopics(), new RankTopic());
			Collections.sort(chapter.getEntities(), new RankEntity());

		}
		
	}







	private static String convertSubToText(List<Line> subtitles) {
        String text = new String();
        for(Line line : subtitles)
            text += line.getText();
		return text;
	}
	
	

	private static void alignTopics(Timeline chapters, ArrayList<TRTopic> topics) {
		int alignedTopic = 0;
		int unAlignedTopic = 0;
		int max_topics = 20;

		System.out.println("Aligning "+ max_topics +" topics to chapters: " );
		for (int i = 0; i < topics.size() && alignedTopic < max_topics;  i++) {
		
			//For every chapter, we try to align the topic.
			boolean aligned = false;
			for(Fragment chapter : chapters.getOrderedFragments()){
				String textChapter = convertSubToText(chapter.getSubtitles());
				if (StringUtils.containsIgnoreCase(textChapter, topics.get(i).getLabel())){
					int frequencyTopic = appearancesText (textChapter, topics.get(i).getLabel());
					topics.get(i).setFrequency(frequencyTopic);
					chapter.getTopics().add(topics.get(i));
					System.out.println("Topic: " + topics.get(i).getLabel() + " has been aligned to a Chapter.");
					aligned = true;
				}
			}
			
			if (!aligned) unAlignedTopic ++; else alignedTopic++;
		
		}
		System.out.println("Topics correctly aligned (" +alignedTopic + ")" );
		System.out.println("Topics wrongly aligned (" +unAlignedTopic + ")" );

		
		
	}

	



	private static void alignSubtitles(Timeline chapters, List<Line> subtitles) {
		int alignedSubtitle = 0;
		int unAlignedSubtitle = 0;
		int cont = 0;

		System.out.println("Aligning "+subtitles.size()+" subtitles to chapters: " );
		for (Line subtitle : subtitles){
			 double start = (double)TimeUnit.MILLISECONDS.toSeconds(subtitle.getFrom()) + ((double)(subtitle.getFrom() - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(subtitle.getFrom())))/1000); 
			 double end = (double)TimeUnit.MILLISECONDS.toSeconds(subtitle.getTo()) + ((double)(subtitle.getTo() - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(subtitle.getTo())))/1000); 

			 //Look for the best fragment:
			 Fragment candidateFragment = chapters.getFragmentAtTime(start);
			 if (candidateFragment != null){
				 if (candidateFragment.getEnd() + (0.10) >= end){
					 candidateFragment.addSubtitle(subtitle);
					 alignedSubtitle ++;
				 }
				 else {
					 System.out.println("ERROR aligning subtitle to chapter... please check data consistence. " + candidateFragment.getEnd() + ", " + end + "   --  " + cont + "    " + start);
					 unAlignedSubtitle++;
				 }
			 }
			 else {
				 System.out.println("ERROR aligning subtitle to chapter... please check data consistence.");
				 unAlignedSubtitle++;
			 }
			 cont++;
		}
		System.out.println("Subtitles correctly aligned (" +alignedSubtitle + ")" );
		System.out.println("Subtitles wrongly aligned (" +unAlignedSubtitle + ")" );

		
	}
	
	
	private static void alignEntities(Timeline chapters, ArrayList<NERDEntity> entities) {

		int alignedEntities = 0;
		int wrongAlignedEntities = 0;
		
		for (NERDEntity entity : entities){

			 //Look for the best fragment:
			 Fragment candidateFragment = chapters.getFragmentAtTime(entity.getStartNPT());
			 if (candidateFragment != null){
				 if (candidateFragment.getEnd() + (0.10) >= entity.getEndNPT()){
					 candidateFragment.getEntities().add(entity);
					 alignedEntities ++;
				 }
				 else{
					 System.out.println("ERROR aligning entity to chapter... please check data consistence. " + candidateFragment.getEnd() + ",  " + entity.getEndNPT());
					 wrongAlignedEntities++;
				 }
			 }
			 else {
				 System.out.println("ERROR aligning entity to chapter... please check data consistence. ");
				 wrongAlignedEntities++;
			 }
		}
		System.out.println("Entities correctly aligned (" +alignedEntities + ")" );
		System.out.println("Entities wrongly aligned (" +wrongAlignedEntities + ")" );

	}

	

	private static int appearancesText(String textChapter, String label) {
		int frequency = 0;
		String text = new String (textChapter);
		while (StringUtils.containsIgnoreCase(text, label)){
			int startIndex = StringUtils.indexOfIgnoreCase(text, label);
			int endIndex = startIndex +label.length();
			//System.out.println("ANTES:" + text);
			text = text.substring(0, startIndex) + text.substring(endIndex);
			//System.out.println("DESPUES:" + text);

			frequency ++;
		}
		return frequency;
	}

	private static List<Line> readSubtitles(File file) {
		
		
		if (file.exists()){

	        TextTrackImpl textTrack = null;
			try {
		        ByteArrayInputStream is = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
				textTrack = SrtParser.parse(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return textTrack.getSubs();
		}
		else{
			System.out.println("ERROR when reading " + file.getAbsolutePath());
			return null;
		}        
	}

	private static Timeline merge(Timeline chapters, Timeline shots) {

		Timeline visual_chapters = new Timeline();
		
		for(Fragment chapter : chapters.getOrderedFragments()){
			
			Fragment shot_Start = shots.getFragmentAtTime(chapter.getStart());
			Fragment shot_End = shots.getFragmentAtTime(chapter.getEnd());
			
			//Increase chapter backwards if shot already started before, and it was not about to finish (then it is too optimistic to take the entire shot)
			//If it was about to finish, we keep original boundaries
			if (Math.abs(shot_Start.getStart() - chapter.getStart()) < (Math.abs(shot_Start.getEnd() - chapter.getStart()))){
				//System.out.println("CHANGE BACK: " + chapter.getStart() + " to " + shot_Start.getStart());
				chapter.setStart(shot_Start.getStart());
			}
			
			//Increase chapter forward if shot is still not finished, and it was not about to start (then it is too optimistic to take the entire shot)
			//If it was about to start, we keep original boundaries
			if (Math.abs(shot_End.getEnd() - chapter.getEnd()) < (Math.abs(shot_End.getStart() - chapter.getEnd()))){
				//System.out.println("CHANGE FORW: " + chapter.getEnd() + " to " + shot_End.getEnd());
				chapter.setEnd(shot_End.getEnd());

			}
			visual_chapters.insertFragment(chapter);
		}

		return visual_chapters;
		
	}

	private static Timeline parseChapters(File file) {
		
		if (file.exists()){
			
			Timeline chapters = new Timeline();
			
			List<String> chapString = null;
			try {
				chapString = FileUtils.readLines(file, "UTF8");
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
			int i = 0;
			for (String chapLine: chapString){
				String[] boundaries = chapLine.split(",");
				
				if(boundaries.length == 2){
					
					
					
					Fragment chapter = new Fragment();
					chapter.setEnd(Float.parseFloat(boundaries[1]));
					chapter.setStart(Float.parseFloat(boundaries[0]));
					
					chapters.insertFragment(chapter);
				}
				else System.out.println("WRONG chapter in line " + i);
				
				i++;
			}
			
			System.out.println("Chapters created.");

			return chapters;
			
		}
		else System.out.println("ERROR when reading " + file.getAbsolutePath());
		return null;
	}

	private static Timeline parseExmeralda(File file) {
		

		
		
		if (file.exists()){
			System.out.println("The file exists.");
			 Processing p = new Processing();
			  VideoMetaData visual_metadata = p.analisys_process(file);
			  

			  
			  					
			  	List<Layer> certh_segments = visual_metadata.getLayersStartingBy("CERTH_Segments-");
					
				Layer layer = extractShotLayer(certh_segments);
				
				
					
				if (layer == null) {
						System.out.println("No information about Shots. ABORTING");
						return null;
				}
				
				
				Timeline shots = new Timeline();
				createShots(layer, shots);
				
				
				System.out.println("Shots created.");
					
				return shots;
		}
		return null;
		
	}


	private static void createShots(Layer layer, Timeline shots) {
		

		for (int i = 0; i < layer.getFragments().size(); i ++){
			ItemLayer fragment = layer.getFragments().get(i);
			
			Fragment shot = new Fragment();
			shot.setEnd(fragment.getEnd());
			shot.setStart(fragment.getStart());
			
			shots.insertFragment(shot);
		}

		
	}

	private static Layer extractShotLayer(List<Layer> certh_segments) {
	
		if (certh_segments == null)  return null;
		for (Layer l : certh_segments){
			
			if (l.getFragments().size() > 0) {
				if (l.getFragments().get(0).getValue().contains("Sh")){
					certh_segments.remove(l);
					return l;
				}
			}
		}
		return null;
	}



}
