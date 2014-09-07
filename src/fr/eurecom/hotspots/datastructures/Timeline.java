package fr.eurecom.hotspots.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl.Line;
import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl;

import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;

public class Timeline {

	Vector <Fragment> timeline = new Vector <Fragment> ();
	private static double epsilon = 0.5;

	
	public void insertFragment(Fragment f){
		boolean inserted = false;
		if (timeline.isEmpty()) 
			timeline.add(f);
		
		else{
			for (int i = 0; i < timeline.size() && !inserted; i ++){
				if(isBefore(f, timeline.get(i))){
					for (int j = timeline.size(); j > i; j--){
						if (j == timeline.size()) timeline.add(f);
						timeline.set(j, timeline.get(j-1)); 
					}
					timeline.set(i, f);
					inserted = true;
				}
			}
			if (!inserted) timeline.add(timeline.size(), f);
		}

	}
	
	private boolean isBefore(Fragment f, Fragment fragment) {
		if (f.getEnd() <= fragment.getStart() + 0.3){
			return true;
		}
		return false;
	}

	public Fragment getFragmentAtTime(double time){
		for (int i = 0; i < timeline.size(); i ++){
			if(timeline.get(i).getStart()-0.10 <= time && timeline.get(i).getEnd() - 0.10 > time) {
				return timeline.get(i);
			}
		}
		return null;
	}
	
	public Vector <Fragment> getOrderedFragments(){
		return timeline;
	}
	
	public void plotSRT(){
		for (int i = 0; i < timeline.size(); i ++){
			System.out.println(i);
			double start = timeline.get(i).getStart();
			double end = timeline.get(i).getEnd();
			
			//System.out.println(start + ", " + end);
			
			long Hour = TimeUnit.SECONDS.toHours((long) start);
			long Min = TimeUnit.SECONDS.toMinutes((long) start) - TimeUnit.HOURS.toMinutes(Hour);
			long Sec = TimeUnit.SECONDS.toSeconds((long) start) - TimeUnit.MINUTES.toSeconds(Min);
			long millis = (long)((start - (double)((long)(start))) * 1000);
			
			
			long HourEnd = TimeUnit.SECONDS.toHours((long) end);
			long MinEnd = TimeUnit.SECONDS.toMinutes((long) end) - TimeUnit.HOURS.toMinutes(HourEnd);
			long SecEnd = TimeUnit.SECONDS.toSeconds((long) end) - TimeUnit.MINUTES.toSeconds(MinEnd);
			long millisEnd = (long)((end - (double)((long)(end))) * 1000);
			
			
			
			System.out.println(String.format("%02d:%02d:%02d,%03d", Hour, Min, Sec, millis) + " --> " +String.format("%02d:%02d:%02d,%03d", HourEnd, MinEnd, SecEnd, millisEnd));
			System.out.println("SEGMENT " + i);
			System.out.println("");
			
			              
		}
		
        TextTrackImpl subTitleEng = new TextTrackImpl();
        subTitleEng.getTrackMetaData().setLanguage("eng");


        subTitleEng.getSubs().add(new TextTrackImpl.Line(5000, 6000, "Five"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(8000, 9000, "Four"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(12000, 13000, "Three"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(16000, 17000, "Two"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(20000, 21000, "one"));
        
        System.out.println(subTitleEng.toString());
        
	}
	

	public int size() {
		return this.timeline.size();
	}

	public void merge(Fragment fragment, Fragment fragment2) {
		
		if (this.timeline.contains(fragment) && this.timeline.contains(fragment2) && Math.abs(fragment.end - fragment2.start) < epsilon){
			Fragment f = new Fragment();
			f.setStart(fragment.getStart());
			f.setEnd(fragment2.getEnd());
			
			
			fragment.getEntities();
			
			Vector<NERDEntity> entityMerge = new Vector <NERDEntity> ();
			entityMerge.addAll(fragment.getEntities());
			for (NERDEntity entity : fragment2.getEntities()){
				NERDEntity candidateEntity = containsEntity(entityMerge, entity);
				if (candidateEntity == null){
					entityMerge.add(entity);
				}
			}
			f.setEntities(entityMerge);
			
		
			Vector<TRTopic> topicMerge = new Vector <TRTopic> ();
			topicMerge.addAll(fragment.getTopics());
			for (TRTopic topic : fragment2.getTopics()){
				TRTopic candidateTopic = containsTopic(topicMerge, topic);
				if (candidateTopic == null){
					topicMerge.add(topic);
				}
				else candidateTopic.setFrequency(candidateTopic.getFrequency()+1);
			}
			f.setTopics(topicMerge);	
			
			
			
			Vector<Line> subtitleMerge = new Vector <Line> ();
			subtitleMerge.addAll(fragment.getSubtitles());
			subtitleMerge.addAll(fragment2.getSubtitles());
			f.setSubtitles(subtitleMerge);
			
			
			//f.setVisualconcepts(visualconcepts);
			
			this.timeline.remove(fragment);
			this.timeline.remove(fragment2);
			this.insertFragment(f);
		}
		else System.out.println("It is impossible to merge the specified fragments");
		
	}

	private NERDEntity containsEntity(Vector<NERDEntity> entityMerge,
			NERDEntity entity) {
		for (NERDEntity e : entityMerge){
			if (StringUtils.equalsIgnoreCase(e.getLabel(), entity.getLabel())){
				return e;
			}
		}
		return null;
	}

	private TRTopic containsTopic(Vector<TRTopic> topicMerge, TRTopic topic) {
		for (TRTopic t : topicMerge){
			if (StringUtils.equalsIgnoreCase(t.getLabel(), topic.getLabel())){
				return t;
			}
		}
		return null;
	}
	
}
