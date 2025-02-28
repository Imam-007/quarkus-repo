package com.imam.resources;

import com.imam.dto.MovieDTO;
import com.imam.services.MovieService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/movies")
public class MovieResource {

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

    @PATCH
    @Path("/{id}")
    public Uni<Response> patchUpdateMovie(@PathParam("id") Long id, MovieDTO movie) {
        return movieService.patchUpdate(id, movie.getName());
    }

    @GET
    @Path("/search")
    public Multi<MovieDTO> getMoviesByName(@QueryParam("name") String name) {
        return movieService.searchMovies(name);
    }

    @GET
    @Path("/recent")
    public Multi<MovieDTO> getRecentMovies() {
        return movieService.getRecentMovies();
    }
}
