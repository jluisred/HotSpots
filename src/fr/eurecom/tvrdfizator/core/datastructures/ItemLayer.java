package fr.eurecom.tvrdfizator.core.datastructures;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

public class ItemLayer {
	private String MediaFragmentURL = "";
	
	
	Hashtable <String, String> table_ud_information = new Hashtable<String, String> ();

	private String value="";
	private float start;
	private float end;
	
	
	
	public String getMediaFragmentURL() {
		return MediaFragmentURL;
	}
	public void setMediaFragmentURL(String mediaFragmentURL) {
		MediaFragmentURL = mediaFragmentURL;
	}
	
	public void set_ud_information(String name, String value){
		table_ud_information.put(name, value);
	}
	
	public String get_ud_information(String name){
		return table_ud_information.get(name);
	}
	public String get_first_ud_information(){
		if (table_ud_information.size() > 0)
			return table_ud_information.values().iterator().next();
		return null;
	}
	
	public Set<Entry<String, String>> get_table_ud_information () {
		return table_ud_information.entrySet();
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public float getStart() {
		return start;
	}
	public void setStart(float start) {
		this.start = start;
	}
	public float getEnd() {
		return end;
	}
	public void setEnd(float end) {
		this.end = end;
	}

	
	
}
