package com.xavelo.filocitas.application.domain;

import java.util.Objects;
import java.util.UUID;

public class Tag {

    private final UUID id;
    private final String name;

    public Tag(UUID id, String name) {
        this.id = id;
        this.name = Objects.requireNonNullElse(name, "").trim();
    }

    public Tag(String name) {
        this(null, name);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Tag withId(UUID id) {
        return new Tag(id, name);
    }
}
