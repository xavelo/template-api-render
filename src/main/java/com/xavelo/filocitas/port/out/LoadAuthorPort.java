package com.xavelo.filocitas.port.out;

import com.xavelo.filocitas.application.domain.author.Author;

import java.util.Optional;
import java.util.UUID;

public interface LoadAuthorPort {

    Optional<Author> findAuthorById(UUID id);
}
