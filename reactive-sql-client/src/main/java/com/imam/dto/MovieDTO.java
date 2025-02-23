package com.imam.dto;

import io.vertx.mutiny.sqlclient.Row;

public class MovieDTO {

    private Long id;
    private String name;

    public MovieDTO() {
    }

    public MovieDTO(String name) {
        this.name = name;
    }

    public MovieDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static MovieDTO from(Row row) {
        return new MovieDTO(row.getLong("id"), row.getString("name"));
    }
}
