package fr.eurecom.hotspots.web.hypertext;


import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.FileUtils;

@Path("/")
public class Index {
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response doGetHTML(  @Context UriInfo ui,
                                @Context SecurityContext context,
                                @CookieParam("token") String token) 
    {
    	
    	Response result = null;

    	
    	String html = null;
    	try {
			 html = FileUtils.readFileToString(new File("ui/index.html"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
        result = Response.ok().entity(html).build();    
        return result;
    }

}
