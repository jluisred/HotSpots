
	package com.github.axet.vget.vhs;

	import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

	import org.apache.commons.lang3.StringEscapeUtils;

	import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.info.VideoInfo;
import com.github.axet.vget.info.VGetParser.VideoDownload;
import com.github.axet.vget.info.VideoInfo.States;
import com.github.axet.vget.info.VideoInfo.VideoQuality;
import com.github.axet.wget.WGet;
import com.github.axet.wget.WGet.HtmlLoader;
import com.github.axet.wget.info.ex.DownloadError;
import com.google.gson.Gson;

	public class TedParser extends VGetParser {

	    URL source;

	    public static class VimeoData {
	        public VimeoRequest request;
	        public VimeoVideo video;
	    }

	    public static class VimeoVideo {
	        public Map<String, String> thumbs;
	        public String title;
	    }

	    public static class VimeoRequest {
	        public String signature;
	        public String session;
	        public long timestamp;
	        public long expires;
	        public VimeoFiles files;
	    }

	    public static class VimeoFiles {
	        public ArrayList<String> codecs;
	        public VidemoCodec h264;
	    }

	    public static class VidemoCodec {
	        public VideoDownloadLink hd;
	        public VideoDownloadLink sd;
	        public VideoDownloadLink mobile;
	    }

	    public static class VideoDownloadLink {
	        public String url;
	        public int height;
	        public int width;
	        public String id;
	        public int bitrate;
	    }

	    public TedParser(URL input) {
	        this.source = input;
	    }

	    public static boolean probe(URL url) {
	        return url.toString().contains("ted.com");
	    }

		@Override
		public List<VideoDownload> extract(VideoInfo info, AtomicBoolean stop,
				Runnable notify) {


	        List<VideoDownload> list = new ArrayList<VGetParser.VideoDownload>();

            list.add(new VideoDownload(VideoQuality.p1080, this.source));
            list.add(new VideoDownload(VideoQuality.p480, this.source));
            
	        return list;
		}



	}
