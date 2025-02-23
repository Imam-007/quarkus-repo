package com.imam.resources;

import com.imam.dao.MovieDAO;
import com.imam.dto.MovieDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/movies")
public class MovieResource {

    @Inject
    PgPool client;

    @GET
    public Multi<MovieDTO> getAllMovie(){
        return MovieDAO.findAllMovies(client);
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getMovieById(@PathParam("id") Long id){
        return MovieDAO.findById(client, id)
                .onItem()
                .transform(movie -> movie != null ? Response.ok(movie) : Response.status(Response.Status.NOT_FOUND))
                .onItem()
                .transform(Response.ResponseBuilder::build);
    }

    @POST
    public Uni<Response> addMovie(MovieDTO movie){
        return MovieDAO.save(client, movie.getName())
                .onItem()
                .transform(id -> URI.create("/movies/"+ id))
                .onItem()
                .transform(uri -> Response.created(uri).build());
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteMovie(@PathParam("id") Long id){
        return MovieDAO.delete(client, id)
                .onItem()
                .transform(deleted -> deleted ? Response.Status.NO_CONTENT : Response.Status.NOT_FOUND)
                .onItem()
                .transform(status -> Response.status(status).build());
    }
}
