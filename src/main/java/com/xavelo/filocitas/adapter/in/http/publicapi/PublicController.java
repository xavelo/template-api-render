package com.xavelo.filocitas.adapter.in.http.publicapi;

import com.xavelo.filocitas.port.in.GetQuoteByIdUseCase;
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

    public PublicController(GetQuoteByIdUseCase getQuoteByIdUseCase) {
        this.getQuoteByIdUseCase = getQuoteByIdUseCase;
    }

    @GetMapping("/quote/{id}")
    public ResponseEntity<QuoteResponse> getQuoteById(@PathVariable("id") UUID id) {
        logger.info("Fetching quote with id {}", id);
        return getQuoteByIdUseCase.getQuoteById(id)
                .map(quote -> ResponseEntity.ok(QuoteResponse.fromDomain(quote)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
