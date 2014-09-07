package fr.eurecom.tvrdfizator.core.datastructures;

import java.util.Vector;

public class Episode {
	private String id;
	private String title;
	private String shortSynopsis;
	private String mediumSynopsis;
	private String longSynopsis;
	private String Broadcaster;


	private Vector<String> memberOfVector = new Vector<String>();
	private Vector<String> keyword_Vector = new Vector<String>();
	private Vector<Material> materials = new Vector<Material>();
	private Vector<Version> versions = new Vector<Version>();

	
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
	
	public String getMediumSynopsis() {
		return mediumSynopsis;
	}

	public void setMediumSynopsis(String mediumSynopsis) {
		this.mediumSynopsis = mediumSynopsis;
	}
	
	public String getBroadcaster() {
		return Broadcaster;
	}

	public void setBroadcaster(String broadcaster) {
		Broadcaster = broadcaster;
	}

	public void add_memberOf(String member){
		memberOfVector.add(member);
	}
	
	public Vector<String> get_membersOf(){
		return memberOfVector;
	}
	
	public void add_keyword(String member){
		keyword_Vector.add(member);
	}
	
	public Vector<String> get_keywords(){
		return keyword_Vector;
	}
	
	public void addMaterial(Material m){
		materials.add(m);
	}
	
	public Vector<Material> getMaterials(){
		return materials;
	}

	public void addVersion(Version v) {
		versions.add(v);
	}
	public Vector<Version> getVersions(){
		return versions;
	}
	
}
