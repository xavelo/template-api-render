package com.xavelo.filocitas.port.out;

import com.xavelo.filocitas.application.domain.Author;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadAuthorPort {

    Optional<Author> findAuthorById(UUID id);

    List<Author> findAllAuthors();

    long countAuthors();
}
