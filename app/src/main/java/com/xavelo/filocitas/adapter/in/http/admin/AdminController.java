package com.xavelo.filocitas.adapter.in.http.admin;

import com.xavelo.filocitas.adapter.in.http.mapper.ApiMapper;
import com.xavelo.filocitas.api.AdminApi;
import com.xavelo.filocitas.api.model.Quote;
import com.xavelo.filocitas.api.model.QuoteRequest;
import com.xavelo.filocitas.port.in.SaveUquoteUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminController implements AdminApi {

    private static final Logger logger = LogManager.getLogger(AdminController.class);

    private final SaveUquoteUseCase saveUquoteUseCase;
    private final ApiMapper apiMapper;

    public AdminController(SaveUquoteUseCase saveUquoteUseCase, ApiMapper apiMapper) {
        this.saveUquoteUseCase = saveUquoteUseCase;
        this.apiMapper = apiMapper;
    }

    @Override
    public ResponseEntity<Quote> createQuote(@Valid @RequestBody QuoteRequest quoteRequest) {
        var domainQuote = apiMapper.toDomainQuote(quoteRequest);
        logger.info("Received quote '{}' from author {}", domainQuote.getText(), domainQuote.getAuthor().getName());
        var savedQuote = saveUquoteUseCase.saveQuote(domainQuote);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiMapper.toApiQuote(savedQuote));
    }

    @Override
    public ResponseEntity<List<Quote>> createQuotes(@Valid @RequestBody @Size(min = 1) List<@Valid QuoteRequest> quoteRequest) {
        var domainQuotes = apiMapper.toDomainQuotes(quoteRequest);
        domainQuotes.forEach(quote -> logger.info("Received quote '{}' from author {}", quote.getText(), quote.getAuthor().getName()));
        var savedQuotes = domainQuotes.stream()
                .map(saveUquoteUseCase::saveQuote)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiMapper.toApiQuotes(savedQuotes));
    }
}
