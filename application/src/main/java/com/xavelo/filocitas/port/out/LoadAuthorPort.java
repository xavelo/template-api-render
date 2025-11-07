package com.xavelo.filocitas.port.out;

import com.xavelo.filocitas.application.domain.Author;
import com.xavelo.filocitas.application.domain.AuthorQuotesSummary;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadAuthorPort {

    Optional<Author> findAuthorById(UUID id);

    List<Author> findAllAuthors();

    long countAuthors();

    List<AuthorQuotesSummary> findTopAuthorsWithQuoteCount(int limit);
}
