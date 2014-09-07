package fr.eurecom.hotspots.videodownload;

import java.io.File;
import java.net.URL;

import com.github.axet.vget.VGet;

public class VideoDownload {

	/*
    public static void main(String[] args) {
        try {
            VGet v = new VGet(new URL("http://vimeo.com/52716355"), new File("./"));
            System.out.println("Starting download");
            v.download();
            System.out.println("Ending download");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    */
    
    
    public static void main(String[] args) {
        System.out.println("Starting download");

        if (args.length == 1) {
        	AppManagedDownload.main(new String[] {args[0] , "./" });
            System.out.println("Ending download");
        }
        
        else System.out.println("Provide URL of the video");

    }
    
    
  /*   public static void main(String[] args) {
       try {
            // ex: http://www.youtube.com/watch?v=Nj6PFaDmp6c
            String url = args[0];
            // ex: "/Users/axet/Downloads"
            String path = args[1];

            VGet v = new VGet(new URL(url), new File(path));

            v.download();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    


}