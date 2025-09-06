package com.xavelo.template.render.api.domain;

import java.util.Objects;
import java.util.UUID;

public record Guardian(UUID id, String name, String email) {
    public Guardian {
        Objects.requireNonNull(email, "email must not be null");
    }
}
