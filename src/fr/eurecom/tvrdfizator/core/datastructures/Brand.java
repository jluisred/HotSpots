package fr.eurecom.tvrdfizator.core.datastructures;

import java.util.Vector;

public class Brand {

	String id;
	String title;
	String shortSynopsis;
	String longSynopsis;
	
	Vector<String> memberOfVector = new Vector<String>();
	Vector<String> episodes = new Vector<String>();

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortSynopsis() {
		return shortSynopsis;
	}

	public void setShortSynopsis(String shortSynopsis) {
		this.shortSynopsis = shortSynopsis;
	}

	public String getLongSynopsis() {
		return longSynopsis;
	}

	public void setLongSynopsis(String longSynopsis) {
		this.longSynopsis = longSynopsis;
	}

	public void add_memberOf(String member){
		memberOfVector.add(member);
	}
	
	public Vector<String> get_memberOf(){
		return memberOfVector;
	}

	public void add_episode(String ep){
		episodes.add(ep);
	}
	
	public Vector<String> get_episodes(){
		return episodes;
	}
	
	public void print(){
		System.out.println("BRAND: ----------------------------------------------------------------");
		System.out.println("  ID: "+id);
		System.out.println("  Title: "+title);
		System.out.println("  Episodes:");
		for (int i = 0; i < episodes.size(); i ++){
			System.out.println("     "+ episodes.get(i));
		}
		System.out.println("  Members:");
		for (int i = 0; i < memberOfVector.size(); i ++){
			System.out.println("     "+ memberOfVector.get(i));
		}
		System.out.println("-----------------------------------------------------------------------");

	}
}
