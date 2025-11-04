package com.xavelo.filocitas.adapter.in.http.publicapi;

import com.xavelo.filocitas.port.in.GetAuthorByIdUseCase;
import com.xavelo.filocitas.port.in.GetQuoteByIdUseCase;
import com.xavelo.filocitas.port.in.GetRandomQuoteUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private static final Logger logger = LogManager.getLogger(PublicController.class);

    private final GetQuoteByIdUseCase getQuoteByIdUseCase;
    private final GetAuthorByIdUseCase getAuthorByIdUseCase;
    private final GetRandomQuoteUseCase getRandomQuoteUseCase;

    public PublicController(GetQuoteByIdUseCase getQuoteByIdUseCase,
                            GetAuthorByIdUseCase getAuthorByIdUseCase,
                            GetRandomQuoteUseCase getRandomQuoteUseCase) {
        this.getQuoteByIdUseCase = getQuoteByIdUseCase;
        this.getAuthorByIdUseCase = getAuthorByIdUseCase;
        this.getRandomQuoteUseCase = getRandomQuoteUseCase;
    }

    @GetMapping("/quote/{id}")
    public ResponseEntity<QuoteResponse> getQuoteById(@PathVariable("id") UUID id) {
        logger.info("Fetching quote with id {}", id);
        return getQuoteByIdUseCase.getQuoteById(id)
                .map(quote -> ResponseEntity.ok(QuoteResponse.fromDomain(quote)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/quote/random")
    public ResponseEntity<QuoteResponse> getRandomQuote() {
        logger.info("Fetching random quote");
        return getRandomQuoteUseCase.getRandomQuote()
                .map(quote -> ResponseEntity.ok(QuoteResponse.fromDomain(quote)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable("id") UUID id) {
        logger.info("Fetching author with id {}", id);
        return getAuthorByIdUseCase.getAuthorById(id)
                .map(author -> ResponseEntity.ok(AuthorResponse.fromDomain(author)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
