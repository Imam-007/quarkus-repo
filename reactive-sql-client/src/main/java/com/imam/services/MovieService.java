package com.imam.services;

import com.imam.dto.MovieDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.core.Response;

public interface MovieService {

    public Multi<MovieDTO> findAllMovies();

    public Uni<Response> findById(Long id);

    public Uni<Response> save(String name);

    public Uni<Response> delete(Long id);

    public Uni<Response> patchUpdate(Long id, String name);

    public Multi<MovieDTO> searchMovies(String name);

    public Multi<MovieDTO> getRecentMovies();
}
