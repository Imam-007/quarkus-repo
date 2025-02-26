package com.imam.dao;

import com.imam.dto.MovieDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import org.jboss.logging.Logger;

public class MovieDAO {
    private static final Logger LOGGER = Logger.getLogger(MovieDAO.class);

    public static Multi<MovieDTO> findAllMovies(PgPool client) {
        String sql = "SELECT id, name FROM movies ORDER BY name DESC";
        LOGGER.info("Executing SQL: " + sql);

        return client
                .query(sql)
                .execute()
                .onItem()
                .transformToMulti(set -> {
                    if (set == null || !set.iterator().hasNext()) {
                        LOGGER.warn("Query returned no results.");
                        return Multi.createFrom().empty();
                    }
                    return Multi.createFrom().iterable(set);
                })
                .onItem()
                .transform(MovieDTO::from)
                .onFailure()
                .invoke(ex -> LOGGER.error("Error executing query: " + sql, ex));
    }

    public static Uni<MovieDTO> findById(PgPool client, Long id) {
        return client
                .preparedQuery("SELECT id, name FROM movies WHERE id = $1")
                .execute(Tuple.of(id))
                .onItem()
                .transform(m -> m.iterator().hasNext() ? MovieDTO.from(m.iterator().next()) : null);
    }

    public static Uni<Long> save(PgPool client, String name){
        return client
                .preparedQuery("INSERT INTO movies (name) VALUES ($1) RETURNING id")
                .execute(Tuple.of(name))
                .onItem()
                .transform(m -> m.iterator().next().getLong("id"));
    }

    public static Uni<Boolean> delete(PgPool client, Long id){
        return client
                .preparedQuery("DELETE FROM movies WHERE id = $1")
                .execute(Tuple.of(id))
                .onItem()
                .transform(row -> row.rowCount() == 1);
    }

    public static Uni<Boolean> update(PgPool client, Long id, String name) {
        return client
                .preparedQuery("UPDATE movies SET name = $1 WHERE id = $2")
                .execute(Tuple.of(name, id))
                .onItem()
                .transform(row -> row.rowCount() == 1);
    }

    public static Multi<MovieDTO> searchByName(PgPool client, String name) {
        return client.preparedQuery("SELECT id, name FROM movies WHERE name ILIKE $1")
                .execute(Tuple.of("%" + name + "%"))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(MovieDTO::from);
    }

    public static Uni<Long> count(PgPool client) {
        return client.query("SELECT COUNT(*) FROM movies")
                .execute()
                .onItem()
                .transform(m -> m.iterator().next().getLong(0));
    }
}
