package org.acme.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/main")
public class MainResource {

    @GET
    @Path("/name")
    public String getName(){
        return "Imam";
    }
}
