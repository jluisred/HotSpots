package fr.eurecom.hotspots.srt;

import java.util.LinkedList;
import java.util.List;

import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl.Line;


import fr.eurecom.tvrdfizator.core.datastructures.NERDEntity;

public class SRTMapper {
    
    public List<NERDEntity> run (List<Line> subtitles, List<NERDEntity> extractions) 
    {   
        List<NERDEntity> result = new LinkedList<NERDEntity>();
        
        for(NERDEntity e : extractions) 
        {
            boolean found = false; 
            int i=0;
            String line = new String();
            int length = line.length(); 
            for(;!found && i< subtitles.size(); i++) {
                line += subtitles.get(i).getText();
                if(e.getStartChar()>=length && e.getStartChar()< line.length()) {
                    found = true; 
                }
                length += subtitles.get(i).getText().length();
            }
            if (!found) System.out.println("Bad Aligned: " + e.getStartChar() + ", "+ e.getEndChar() +"    "+e.getLabel());
            e.setStartNPT( new Double (1.0 * subtitles.get(i-1).getFrom()/1000) );
            e.setEndNPT( new Double (1.0 * subtitles.get(i-1).getTo()/1000) );  
            
            result.add(e);
        }
        
        return result;
    }
}
