package fr.eurecom.hotspots.web.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl;
import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl.Line;
import com.googlecode.mp4parser.srt.SrtParser;

import fr.eurecom.hotspots.core.HotSpotGenerator;




@Path("/hotspots")
public class HotSpots {

	
	
	
	@POST
	@Path("/generate")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response doPOSTMETADATA(String subtitleFile,
			@QueryParam("UUID") String uuid,
			@QueryParam("videoURL") String videoURL,
			@QueryParam("visualAnalysis") String visualEnabled,
			@QueryParam("chapterList") String chapterList) {
 
		
		System.out.println("Got request for processing HotSpots... " + videoURL);
		
		if (chapterList == null){
			return Response.status(400).entity("No Chapters available. ABORTING.").header("Access-Control-Allow-Origin", "*").build();
		}
		else System.out.println("CHAPTERS " + chapterList);
		
		if (subtitleFile.equals("") || !checkSubtitles(subtitleFile)){
			return Response.status(400).entity("Subtitles are not provided or come in the wrong format (please use SRT). ABORTING.").header("Access-Control-Allow-Origin", "*").build();
		}
		else System.out.println("Subtitles are OK." );
		
		//Parse UUID
		UUID idMediaResource = null;
		try {
			idMediaResource = UUID.fromString(uuid);
		} catch (IllegalArgumentException e) {
			System.out.println("Illegal UUID ");
			e.printStackTrace();
			return Response.status(501).entity("Illegal UUID").header("Access-Control-Allow-Origin", "*").build();
		}
		
		//
		boolean visualEnabledBool  = false;
		if (visualEnabled != null){
			if (visualEnabled.equals("true")) visualEnabledBool = true;
		}
		
		HotSpotGenerator hpG = new HotSpotGenerator (videoURL, visualEnabledBool, subtitleFile, chapterList, idMediaResource.toString());
		String resultsjson = hpG.generate();
		
	    return Response.status(200).entity(resultsjson).header("Access-Control-Allow-Origin", "*").build();

	}
	
	private boolean checkSubtitles(String SRT) {

		try {
			ByteArrayInputStream is = new ByteArrayInputStream(SRT.getBytes());
			SrtParser.parse(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;

	}

}