package com.xavelo.filocitas.application.domain;

import java.util.Objects;
import java.util.UUID;

public class Author {

    private final UUID id;
    private final String name;
    private final String wikipediaUrl;

    public Author(UUID id, String name, String wikipediaUrl) {
        this.id = id;
        this.name = Objects.requireNonNullElse(name, "");
        this.wikipediaUrl = Objects.requireNonNullElse(wikipediaUrl, "");
    }

    public Author(String name, String wikipediaUrl) {
        this(null, name, wikipediaUrl);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWikipediaUrl() {
        return wikipediaUrl;
    }

    public Author withId(UUID id) {
        return new Author(id, name, wikipediaUrl);
    }

}
