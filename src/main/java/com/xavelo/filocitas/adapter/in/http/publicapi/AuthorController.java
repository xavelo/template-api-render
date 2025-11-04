package com.xavelo.filocitas.adapter.in.http.publicapi;

import com.xavelo.filocitas.port.in.GetAllAuthorsUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthorController {

    private static final Logger logger = LogManager.getLogger(AuthorController.class);

    private final GetAllAuthorsUseCase getAllAuthorsUseCase;

    public AuthorController(GetAllAuthorsUseCase getAllAuthorsUseCase) {
        this.getAllAuthorsUseCase = getAllAuthorsUseCase;
    }

    @GetMapping("/api/authors")
    public ResponseEntity<List<AuthorResponse>> getAuthors() {
        logger.info("Fetching all authors");
        List<AuthorResponse> authors = getAllAuthorsUseCase.getAuthors().stream()
                .map(AuthorResponse::fromDomain)
                .collect(Collectors.toList());

        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(authors);
    }
}
