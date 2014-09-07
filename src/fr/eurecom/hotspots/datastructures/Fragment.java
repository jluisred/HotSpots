package fr.eurecom.hotspots.datastructures;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl.Line;

import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;
import fr.eurecom.tvrdfizator.core.datastructures.VisualConcept;

public class Fragment {

	double start;
	double end;
	
	double relevance;
	
	private static double epsilon = 0.1;
	
	Vector <NERDEntity> entities= new Vector<NERDEntity> ();
	Vector <TRTopic> topics= new Vector<TRTopic> ();
	Vector <VisualConcept> visualconcepts= new Vector<VisualConcept> ();
	Vector <Line> subtitles= new Vector<Line> ();

	
	public double getStart() {
		return start;
	}


	public void setStart(double start) {
		this.start = start;
	}


	public double getEnd() {
		return end;
	}


	public void setEnd(double end) {
		this.end = end;
	}


	public Vector<NERDEntity> getEntities() {
		return entities;
	}


	public void setEntities(Vector<NERDEntity> entities) {
		this.entities = entities;
	}


	public Vector<TRTopic> getTopics() {
		return topics;
	}


	public void setTopics(Vector<TRTopic> topics) {
		this.topics = topics;
	}


	public Vector<VisualConcept> getVisualconcepts() {
		return visualconcepts;
	}


	public void setVisualconcepts (Vector<VisualConcept> visualconcepts) {
		this.visualconcepts = visualconcepts;
	}


	public void mergeWithFragment(Fragment b){
		
		if(areConsecutive(b)){
			entities.addAll(b.getEntities());
			topics.addAll(b.getTopics());
			visualconcepts.addAll(b.getVisualconcepts());
			
			if(isAfter(b)){
				this.start = b.start;
			}
			else{
				this.end = b.end;
			}
		}
	}


	public boolean areConsecutive(Fragment b) {
		
		
		if (isAfter(b)) return true;
		if (isBefore(b)) return true;
		return false;
	}


	private boolean isBefore(Fragment b) {
		if (Math.abs(b.start - this.end) < epsilon) return true;
		return false;
	}


	private boolean isAfter(Fragment b) {
		if (Math.abs(b.end - this.start)  < epsilon) return true;
		return false;
	}


	public Vector<Line> getSubtitles() {
		return subtitles;
	}


	public void setSubtitles(Vector<Line> subtitles) {
		this.subtitles = subtitles;
	}
	
	public void addSubtitle(Line l){
		this.subtitles.add(l);
		Collections.sort(subtitles, new SubtitleComparator());
	}


	public double getRelevance() {
		return relevance;
	}


	public void setRelevance(double relevance) {
		this.relevance = relevance;
	}


	
}

class SubtitleComparator implements Comparator<Line> {
	public int compare(Line e1, Line e2) {
		return (int) (e2.getFrom() - e1.getFrom());
	}
}


