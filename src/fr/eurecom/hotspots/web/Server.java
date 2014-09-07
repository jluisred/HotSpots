package fr.eurecom.hotspots.web;


import java.io.IOException;

import com.sun.grizzly.http.SelectorThread;
import com.sun.grizzly.http.embed.GrizzlyWebServer;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.jersey.spi.container.servlet.ServletContainer;




public class Server {      
            
    public static void main(String[] args) {
              
        // static content is linked from here
        GrizzlyWebServer server = new GrizzlyWebServer(8010);
        
        
        
        //For very slow response extractors:
        SelectorThread st = server.getSelectorThread();
        st.setTransactionTimeout(700000);
        
        
       
        //create UI resources
        ServletAdapter ui = new ServletAdapter();
        ui.addInitParameter(
              "javax.ws.rs.Application",
              "fr.eurecom.hotspots.web.hypertext.RegisterUI");
        ui.setServletInstance(new ServletContainer());

        // REST resources       
        ServletAdapter api = new ServletAdapter();
        api.addInitParameter(
              "javax.ws.rs.Application",
              "fr.eurecom.hotspots.web.api.RegisterAPI");
        api.setContextPath("/api");
        api.setServletInstance(new ServletContainer());
        

        
        // static content adapter
        ServletAdapter staticContent = new ServletAdapter("ui");
        staticContent.setContextPath("/");
        staticContent.setHandleStaticResources(true);
                
        // register all above defined adapters
        server.addGrizzlyAdapter(api, new String[] {"/api"});
        server.addGrizzlyAdapter(staticContent, new String[] {"/"});
        
        
              
        // let's Grizzly run
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}