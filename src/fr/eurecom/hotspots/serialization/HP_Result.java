package fr.eurecom.hotspots.serialization;

import java.util.ArrayList;

public class HP_Result {
	String UUID;
	ArrayList <HotSpot> hp_list = new ArrayList <HotSpot>();
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public ArrayList<HotSpot> getHp_list() {
		return hp_list;
	}
	public void setHp_list(ArrayList<HotSpot> hp_list) {
		this.hp_list = hp_list;
	}
	
	
}
