package fr.eurecom.hotspots.core;

import org.apache.commons.lang3.StringUtils;

import fr.eurecom.hotspots.datastructures.Fragment;
import fr.eurecom.hotspots.datastructures.TRTopic;
import fr.eurecom.hotspots.datastructures.Timeline;
import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;

public class FragmentClustering {

	public void cluster(Timeline visual_chapters) {
		
		boolean changes = false;

		do{
			changes = false;
			
			for (int i = 0 ; i < visual_chapters.getOrderedFragments().size() - 1; i++){
				if (compare (visual_chapters.getOrderedFragments().get(i), visual_chapters.getOrderedFragments().get(i+1))){
					System.out.println("MERGING chapters: (" + i + ", " + (i+1) + ").");
					visual_chapters.merge(visual_chapters.getOrderedFragments().get(i), visual_chapters.getOrderedFragments().get(i+1));
					changes = true;
					System.out.println("SIZE after merging: " + visual_chapters.size());
				}
			}
			System.out.println("{{{{}}}}");
		}while(changes);

	}

	private boolean compare(Fragment fragment, Fragment fragment2) {
		
		double weightTopic = 0.4;
		double weightEntity = 0.6;

		double threshold = 0.25;
		
		
		//Intersection Topics
		int topic_intersection = 0;
		for (TRTopic t : fragment.getTopics()){
			for (TRTopic t2 : fragment2.getTopics()){
				if (LetterPairSimilarity.compareStrings(t.getLabel(), t2.getLabel()) > 0.7){
					topic_intersection++;
				}
			}
		}
		
		//Max Topics
		int max_topic = fragment.getTopics().size();
		if (fragment.getTopics().size() < fragment2.getTopics().size() ) max_topic = fragment2.getTopics().size();
		

		//Intersection Entities
		int entity_intersection = 0;
		for (NERDEntity e : fragment.getEntities()){
			for (NERDEntity e2 : fragment2.getEntities()){
				if (LetterPairSimilarity.compareStrings(e.getLabel(), e2.getLabel()) > 0.7){
					entity_intersection++;
				}
			}
		}
		
		//Max Entities
		int max_entity = fragment.getEntities().size();
		if (fragment.getEntities().size() < fragment2.getEntities().size() ) max_entity = fragment2.getEntities().size();
		

		double similarity = weightTopic*((double)topic_intersection/(double)max_topic) + weightEntity*((double)entity_intersection/(double)max_entity);
	
		System.out.println("Similarity: " + similarity + "      (" + topic_intersection + ", "+entity_intersection + ", " + max_topic  +", " + max_entity);
		if (similarity > threshold )return true;
		else return false;
	}

}
