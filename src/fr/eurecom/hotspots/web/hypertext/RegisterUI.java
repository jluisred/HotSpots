package fr.eurecom.hotspots.web.hypertext;


import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("/")
public class RegisterUI extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();        
        
        classes.add(Index.class);
        
        return classes;
    }
}
    
