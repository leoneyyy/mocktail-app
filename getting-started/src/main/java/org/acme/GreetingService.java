package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;

@ApplicationScoped
public class GreetingService {

    public String greeting(String name){
        return "hello " + name;
    }
}
