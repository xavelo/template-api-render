package com.xavelo.filocitas.application.domain.quote;

import com.xavelo.filocitas.application.domain.author.Author;
import com.xavelo.filocitas.application.domain.tag.Tag;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Quote {

    private final UUID id;
    private final Author author;
    private final String work;
    private final Integer year;
    private final String text;
    private final List<Tag> tags;
    private final String century;

    public Quote(
            UUID id,
            Author author,
            String work,
            Integer year,
            String text,
            List<Tag> tags,
            String century
    ) {
        this.id = id;
        this.author = Objects.requireNonNull(author, "author must not be null");
        this.work = Objects.requireNonNullElse(work, "");
        this.year = year;
        this.text = Objects.requireNonNullElse(text, "");
        this.tags = Collections.unmodifiableList(tags == null ? List.of() : List.copyOf(tags));
        this.century = Objects.requireNonNullElse(century, "");
    }

    public Quote(
            Author author,
            String work,
            Integer year,
            String text,
            List<Tag> tags,
            String century
    ) {
        this(
                null,
                author,
                work,
                year,
                text,
                tags,
                century
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

    public String getText() {
        return text;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public String getCentury() {
        return century;
    }

    public Quote withAuthor(Author author) {
        return new Quote(
                id,
                author,
                work,
                year,
                text,
                tags,
                century
        );
    }

    public Quote withId(UUID id) {
        return new Quote(
                id,
                author,
                work,
                year,
                text,
                tags,
                century
        );
    }
}
