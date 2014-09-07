package fr.eurecom.tvrdfizator.core.readers;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.io.IOUtils;

import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl;
import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl.Line;
import com.googlecode.mp4parser.srt.SrtParser;

import fr.eurecom.tvrdfizator.core.datastructures.Subtitle;

public class SubtitleReader {

	
	private String file;
	
	public SubtitleReader(String f){
		file = f;
	}
	
	public  List<Subtitle> read() throws IOException{

		

		List<Subtitle> subtitles = new ArrayList<Subtitle>();
		List<Line> subs = null;
		//Read SRT File
		InputStream is;

			String text = read_text();
			System.out.println(text);
			is = new ByteArrayInputStream(text.getBytes());
			TextTrackImpl textTrack = SrtParser.parse(is);
			subs = textTrack.getSubs();

		
		//Create subtitles from Lines
		for (int i = 0; i< subs.size(); i++){
			Line l = subs.get(i);

			Subtitle sb = new Subtitle();
			sb.setText(l.getText());
			sb.setStartTmp(((float)(l.getFrom()))/1000);
			sb.setEndTmp(((float)l.getTo())/1000);
			
			
			System.out.println("Subtitle " + i + " (" + sb.getStartTmp() + ", " + sb.getEndTmp()+").");
			//System.out.println(sb.getText());
			if (sb.getText().contains("Tipps f"))
				System.out.println(sb.getText());
			//System.out.println(sb.getText());
			subtitles.add(sb);
		}

		return subtitles;

	}

	private String read_text() throws FileNotFoundException {
		
		String text = "";
		
		System.out.println(file);
	    FileInputStream inputStream = new FileInputStream(file);
	    try {
	        text = IOUtils.toString(inputStream);
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        try {
				inputStream.close();
			} catch (IOException e) {
				System.out.println("Problem reading Subtitle file.");
				e.printStackTrace();
			}
	    }
		return text;
	}
}
