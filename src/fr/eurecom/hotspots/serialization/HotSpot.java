package fr.eurecom.hotspots.serialization;

import java.util.ArrayList;
import java.util.List;

import fr.eurecom.hotspots.datastructures.TRTopic;
import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;

public class HotSpot {
	double startNPT;
	double endNPT;
	
	List <TRTopic> topic_list = new ArrayList <TRTopic>();
	List <NERDEntity> entity_list = new ArrayList <NERDEntity>();
	List <String> visualConcept_list = new ArrayList <String>();
	public double getStartNPT() {
		return startNPT;
	}
	public void setStartNPT(double startNPT) {
		this.startNPT = startNPT;
	}
	public double getEndNPT() {
		return endNPT;
	}
	public void setEndNPT(double endNPT) {
		this.endNPT = endNPT;
	}
	public List<TRTopic> getTopic_list() {
		return topic_list;
	}
	public void setTopic_list(List<TRTopic> topic_list) {
		this.topic_list = topic_list;
	}
	public List<NERDEntity> getEntity_list() {
		return entity_list;
	}
	public void setEntity_list(List<NERDEntity> list) {
		this.entity_list = list;
	}
	public List<String> getVisualConcept_list() {
		return visualConcept_list;
	}
	public void setVisualConcept_list(List<String> visualConcept_list) {
		this.visualConcept_list = visualConcept_list;
	}


}
