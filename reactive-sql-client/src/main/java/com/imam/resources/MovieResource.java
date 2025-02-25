package com.imam.resources;

import com.imam.dto.MovieDTO;
import com.imam.services.MovieService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/movies")
public class MovieResource {

    @Inject
    PgPool client;

    @Inject
    MovieService movieService;

    @GET
    public Multi<MovieDTO> getAllMovie(){
        return movieService.findAllMovies();
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getMovieById(@PathParam("id") Long id){
        return movieService.findById(id);
    }

    @POST
    public Uni<Response> addMovie(MovieDTO movie){
        return movieService.save(movie.getName());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteMovie(@PathParam("id") Long id){
        return movieService.delete(id);
    }
}
