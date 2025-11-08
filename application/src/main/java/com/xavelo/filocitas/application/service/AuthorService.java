package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.Author;
import com.xavelo.filocitas.application.domain.AuthorQuotesSummary;
import com.xavelo.filocitas.port.in.GetAllAuthorsUseCase;
import com.xavelo.filocitas.port.in.GetAuthorByIdUseCase;
import com.xavelo.filocitas.port.in.GetAuthorsCountUseCase;
import com.xavelo.filocitas.port.in.GetAuthorsQuotesCountUseCase;
import com.xavelo.filocitas.port.out.LoadAuthorPort;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService implements GetAuthorByIdUseCase,
        GetAllAuthorsUseCase,
        GetAuthorsCountUseCase,
        GetAuthorsQuotesCountUseCase {

    private final LoadAuthorPort loadAuthorPort;

    public AuthorService(LoadAuthorPort loadAuthorPort) {
        this.loadAuthorPort = loadAuthorPort;
    }

    @Override
    public Optional<Author> getAuthorById(UUID id) {
        return loadAuthorPort.findAuthorById(id);
    }

    @Override
    @Cacheable("authors")
    public List<Author> getAuthors() {
        return loadAuthorPort.findAllAuthors();
    }

    @Override
    public long getAuthorsCount() {
        return loadAuthorPort.countAuthors();
    }

    @Override
    public List<AuthorQuotesSummary> getTopAuthorsQuotesCount(int limit) {
        return loadAuthorPort.findTopAuthorsWithQuoteCount(limit);
    }
}
