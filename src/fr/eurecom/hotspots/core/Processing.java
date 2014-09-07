package fr.eurecom.hotspots.core;


import java.io.File;
import java.util.List;

import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;
import fr.eurecom.tvrdfizator.core.datastructures.Subtitle;
import fr.eurecom.tvrdfizator.core.datastructures.VideoMetaData;
import fr.eurecom.tvrdfizator.core.readers.EntityReader;
import fr.eurecom.tvrdfizator.core.readers.ExmaraldaReader;
import fr.eurecom.tvrdfizator.core.readers.SubtitleReader;


public class Processing {

	
	
	
	
	public VideoMetaData analisys_process(File in_exmaralda_file_path){
		
		//Generate the data structures
		VideoMetaData mdata = new VideoMetaData();	
		

		// - EXMARaLDA file.
		if (!in_exmaralda_file_path.equals(""))
			try {
				ExmaraldaReader er = new ExmaraldaReader (in_exmaralda_file_path, mdata);	
				er.read();
			} catch (Exception e) {
				e.printStackTrace();
			}
		else System.out.println("No analysis information available. Continuing processing XML. ");
		

		return mdata;	
	}
	
	
	public boolean entity_process(String in_entity_file_path, String in_subtitle_file_path, String out_entity_file_path, String media_resource_id, String namespace, String locator, String subtitleFile){
		
		List <Subtitle> subtitles = null;
		// - Entities file.
		if (!in_subtitle_file_path.equals(""))
			try {
				SubtitleReader subtitler = new SubtitleReader (in_subtitle_file_path);	
				subtitles = subtitler.read();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ERROR when generating the RDF model.");
				return false;
			}
		else System.out.println("No subtitle information available. Continuing processing XML. ");
		System.out.println("The number of subtitles is " + subtitles.size());

		
		List <NERDEntity> entities = null;

		// - Entities file.
		if (!in_entity_file_path.equals(""))
			try {
				EntityReader entityr = new EntityReader (in_entity_file_path);	
				entities = entityr.read();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ERROR when generating the RDF model.");
				return false;
			}
		else System.out.println("No entity information available. Continuing processing XML. ");
		

/*		//Write the RDF Data from subtitles and entities
		try {
			RDFWriterSubtitleEntity wt = new RDFWriterSubtitleEntity (out_entity_file_path, subtitles, entities, media_resource_id, namespace, locator, subtitleFile);	
			wt.create_subtitles_entities();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR when generating the RDF model.");
			return false;
		}
*/
		
		System.out.println("RDF model generated sucessfully.");
		return true;
		
		
	}
	
	
	
	
	
}
