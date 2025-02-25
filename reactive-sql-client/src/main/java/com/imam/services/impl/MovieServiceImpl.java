package com.imam.services.impl;

import com.imam.dao.MovieDAO;
import com.imam.dto.MovieDTO;
import com.imam.services.MovieService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@ApplicationScoped
public class MovieServiceImpl implements MovieService {

    @Inject
    PgPool client;

    @Override
    public Multi<MovieDTO> findAllMovies() {
        return MovieDAO.findAllMovies(client);
    }

    @Override
    public Uni<Response> findById(Long id) {
        return MovieDAO.findById(client, id)
                .onItem()
                .transform(movie -> movie != null ? Response.ok(movie) : Response.status(Response.Status.NOT_FOUND))
                .onItem()
                .transform(Response.ResponseBuilder::build);
    }

    @Override
    public Uni<Response> save(String name) {
        return MovieDAO.save(client, name)
                .onItem()
                .transform(id -> URI.create("/movies/"+ id))
                .onItem()
                .transform(uri -> Response.created(uri).build());
    }

    @Override
    public Uni<Response> delete(Long id) {
        return MovieDAO.delete(client, id)
                .onItem()
                .transform(deleted -> deleted ? Response.Status.NO_CONTENT : Response.Status.NOT_FOUND)
                .onItem()
                .transform(status -> Response.status(status).build());
    }
}
