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

    // Get all movies
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<MovieDTO> getAllMovies() {
        return movieService.findAllMovies();
    }

    // Get movie by ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getMovieById(@PathParam("id") Long id) {
        return movieService.findById(id)
                .onItem().transform(movie -> movie != null ?
                        Response.ok(movie).build() :
                        Response.status(Response.Status.NOT_FOUND).build());
    }

    // Add a new movie
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addMovie(MovieDTO movie) {
        return movieService.save(movie.getName())
                .onItem().transform(createdMovie -> Response.status(Response.Status.CREATED).entity(createdMovie).build());
    }

    // Delete a movie by ID
    @DELETE
    @Path("/{id}")
    public Uni<Response> deleteMovie(@PathParam("id") Long id) {
        return movieService.delete(id)
                .onItem().transform(deleted -> deleted != null ?
                        Response.status(Response.Status.NO_CONTENT).build() :
                        Response.status(Response.Status.NOT_FOUND).build());
    }

    // Patch update a movie by ID
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> patchUpdateMovie(@PathParam("id") Long id, MovieDTO movie) {
        return movieService.patchUpdate(id, movie.getName())
                .onItem().transform(updatedMovie -> updatedMovie != null ?
                        Response.ok(updatedMovie).build() :
                        Response.status(Response.Status.NOT_FOUND).build());
    }

    // Search movies by name
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<MovieDTO> getMoviesByName(@QueryParam("name") String name) {
        return movieService.searchMovies(name);
    }

    // Get movie count
    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Long> getMovieCount() {
        return movieService.getMovieCount();
    }

    // Get recent movies
    @GET
    @Path("/recent")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<MovieDTO> getRecentMovies() {
        return movieService.getRecentMovies();
    }
}
