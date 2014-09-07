package fr.eurecom.hotspots.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl.Line;

import fr.eurecom.hotspots.core.HotSpotGenerator;
import fr.eurecom.hotspots.datastructures.Timeline;

public class main {

	

	public static void main(String[] args) {
		
		 String subtitleString = null;
		 String chapterString = null;

		//Parse Chapters from TED
		 try {
			subtitleString =  FileUtils.readFileToString(new File("./alanna_shaikh/alanna_shaikh.srt"));
			 chapterString = FileUtils.readFileToString(new File ("./alanna_shaikh/alanna_shaikh.ch"));

			 System.out.println(chapterString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HotSpotGenerator hpG = new HotSpotGenerator ("http://linkedtv.eurecom.fr/video/6c75499e-d840-4914-ad2b-d5ff9511c7f7", false, subtitleString, chapterString, "6c75499e-d840-4914-ad2b-d5ff9511c7f7");
		String resultsjson = hpG.generate();
		
		 System.out.println(resultsjson);
 
	}
}
