package org.acme.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/main")
public class MainResource {

    @GET
    public String getName(){
        return "i am imam";
    }

    @GET
    @Path("/role")
    public String getRole(){
        return "i am a sde";
    }
}
