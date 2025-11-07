package com.xavelo.filocitas.application.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Quote {

    private final UUID id;
    private final Author author;
    private final String work;
    private final Integer year;
    private final String quote;
    private final List<Tag> tags;
    private final String century;
    private final long likes;

    public Quote(
            UUID id,
            Author author,
            String work,
            Integer year,
            String quote,
            List<Tag> tags,
            String century,
            long likes
    ) {
        this.id = id;
        this.author = Objects.requireNonNull(author, "author must not be null");
        this.work = Objects.requireNonNullElse(work, "");
        this.year = year;
        this.quote = Objects.requireNonNullElse(quote, "");
        this.tags = Collections.unmodifiableList(tags == null ? List.of() : List.copyOf(tags));
        this.century = Objects.requireNonNullElse(century, "");
        this.likes = likes;
    }

    public Quote(
            Author author,
            String work,
            Integer year,
            String quote,
            List<Tag> tags,
            String century
    ) {
        this(
                null,
                author,
                work,
                year,
                quote,
                tags,
                century,
                0L
        );
    }

    public UUID getId() {
        return id;
    }

    public Author getAuthor() {
        return author;
    }

    public String getWork() {
        return work;
    }

    public Integer getYear() {
        return year;
    }

    public String getQuote() {
        return quote;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public String getCentury() {
        return century;
    }

    public long getLikes() {
        return likes;
    }

    public Quote withAuthor(Author author) {
        return new Quote(
                id,
                author,
                work,
                year,
                quote,
                tags,
                century,
                likes
        );
    }

    public Quote withId(UUID id) {
        return new Quote(
                id,
                author,
                work,
                year,
                quote,
                tags,
                century,
                likes
        );
    }

    public Quote withTags(List<Tag> tags) {
        return new Quote(
                id,
                author,
                work,
                year,
                quote,
                tags,
                century,
                likes
        );
    }

    public Quote withLikes(long likes) {
        return new Quote(
                id,
                author,
                work,
                year,
                quote,
                tags,
                century,
                likes
        );
    }
}
