package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.Author;

import java.util.Optional;
import java.util.UUID;

public interface GetAuthorByIdUseCase {

    Optional<Author> getAuthorById(UUID id);
}
