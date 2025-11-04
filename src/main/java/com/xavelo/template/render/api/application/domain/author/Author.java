package com.xavelo.template.render.api.application.domain.author;

import java.util.Objects;

public class Author {

    private final String name;
    private final String wikipediaUrl;

    public Author(String name, String wikipediaUrl) {
        this.name = Objects.requireNonNullElse(name, "");
        this.wikipediaUrl = Objects.requireNonNullElse(wikipediaUrl, "");
    }

    public String getName() {
        return name;
    }

    public String getWikipediaUrl() {
        return wikipediaUrl;
    }

}
