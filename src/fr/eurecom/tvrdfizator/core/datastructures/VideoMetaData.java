package fr.eurecom.tvrdfizator.core.datastructures;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

public class VideoMetaData {
	
	private Brand brand = null;
	private Episode episode = null;
	private Version version = null;

	private Hashtable <String, Layer>layers = new Hashtable <String, Layer>();
	private Vector<Speaker> speakers = new Vector <Speaker>();
	private String videoURL = "";

	
	public VideoMetaData(){
	}
	 

	public void setBrand(Brand brand) {
		this.brand = brand;
	}


	public void setEpisode(Episode episode) {
		this.episode = episode;
	}


	public void setVersion(Version version) {
		this.version = version;
	}


	public Brand getBrand() {
		return brand;
	}

	public Episode getEpisode() {
		return episode;
	}

	public Version getVersion() {
		return version;
	}
	
	public void addLayer(Layer l){
		layers.put(l.getName(), l);
	}
	public Layer getLayer(String name){
		return layers.get(name);
	}
	
	public boolean existLayer(String name){
		return layers.containsKey(name);
	}

	public Vector<Speaker> getSpeakers(){
		return speakers;
	}
	public void addSpeaker(Speaker spk) {
		this.speakers.add(spk);		
	}

	public String getVideoURL() {
		return videoURL;
	}


	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}
	
	public void printLayersNames(){
		for (String l : layers.keySet()){
			System.out.println("  -> [layer] "+ l);
		}
	}


	public List<Layer> getLayersStartingBy(String string) {

		List <Layer> candidateLayers = new ArrayList <Layer> ();
		
		for (Entry<String, Layer> entry : this.layers.entrySet()){
			if (entry.getKey().startsWith(string)) candidateLayers.add(entry.getValue());
		}
		return candidateLayers;
	}
}
