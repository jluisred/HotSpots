package fr.eurecom.tvrdfizator.core.datastructures;

import java.util.Vector;

public class Speaker {
	
	private String id;
	private String sex;
	private String abbreviation;
	private String name;
	private Vector <String> languages = new Vector <String> ();
	private String comment;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Vector<String> getLanguages() {
		return languages;
	}

	public void addLanguage(String l){
		this.languages.add(l);
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	
	
	public void print() {
		System.out.println("Speaker: ----------------------------------------------------");
		System.out.println("  ID: "+ id);
		System.out.println("  Abbreviation: "+abbreviation);
		System.out.println("  Sex: "+ sex);
		System.out.println("  Name: "+name);
		System.out.println("  Languajes: "+ languages);
		System.out.println("-------------------------------------------------------------");

	}
}
