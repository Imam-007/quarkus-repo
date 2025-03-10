package com.imam.services.impl;

import com.imam.dao.MovieDAO;
import com.imam.dto.MovieDTO;
import com.imam.services.MovieService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@ApplicationScoped
public class MovieServiceImpl implements MovieService {

    @Inject
    MySQLPool client;

    @Override
    public Multi<MovieDTO> findAllMovies() {
        return MovieDAO.findAllMovies(client);
    }

    @Override
    public Uni<Response> findById(Long id) {
        return MovieDAO.findById(client, id)
                .onItem().transform(movie -> movie != null ?
                        Response.ok(movie).build() :
                        Response.status(Response.Status.NOT_FOUND).build());
    }

    @Override
    public Uni<Response> save(String name) {
        return MovieDAO.save(client, name)
                .onItem()
                .transform(id -> URI.create("/movies/" + id))
                .onItem()
                .transform(uri -> Response.created(uri).build());
    }

    @Override
    public Uni<Response> delete(Long id) {
        return MovieDAO.delete(client, id)
                .onItem()
                .transform(deleted -> deleted ?
                        Response.status(Response.Status.NO_CONTENT).build() :
                        Response.status(Response.Status.NOT_FOUND).build());
    }

    @Override
    public Uni<Response> patchUpdate(Long id, String name) {
        return MovieDAO.update(client, id, name)
                .onItem()
                .transform(updated -> updated ?
                        Response.ok().build() :
                        Response.status(Response.Status.NOT_FOUND).build());
    }

    @Override
    public Multi<MovieDTO> searchMovies(String name) {
        return MovieDAO.searchByName(client, name);
    }

    @Override
    public Uni<Long> getMovieCount() {
        return MovieDAO.count(client);
    }

    @Override
    public Multi<MovieDTO> getRecentMovies() {
        return MovieDAO.findRecentMovies(client);
    }
}
