package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.author.Author;
import com.xavelo.filocitas.port.in.GetAuthorByIdUseCase;
import com.xavelo.filocitas.port.out.LoadAuthorPort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService implements GetAuthorByIdUseCase {

    private final LoadAuthorPort loadAuthorPort;

    public AuthorService(LoadAuthorPort loadAuthorPort) {
        this.loadAuthorPort = loadAuthorPort;
    }

    @Override
    public Optional<Author> getAuthorById(UUID id) {
        return loadAuthorPort.findAuthorById(id);
    }
}
