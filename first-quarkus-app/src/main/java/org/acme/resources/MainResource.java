package org.acme.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/main")
public class MainResource {

    @GET
    public String getName(){
        return "i am imam.";
    }

    @GET
    @Path("/org")
    public String getNameByOrg(){
        return "imam";
    }
}
