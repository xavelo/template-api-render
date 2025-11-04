package com.xavelo.filocitas.adapter.in.http.publicapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xavelo.filocitas.application.domain.author.Author;

import java.util.UUID;

public record AuthorResponse(
        @JsonProperty("id") UUID id,
        @JsonProperty("name") String name,
        @JsonProperty("wikipedia") String wikipedia
) {

    public static AuthorResponse fromDomain(Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getWikipediaUrl()
        );
    }
}
