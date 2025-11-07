package com.xavelo.filocitas.adapter.in.http.admin;

import com.xavelo.filocitas.adapter.in.http.mapper.ApiMapper;
import com.xavelo.filocitas.api.AdminApi;
import com.xavelo.filocitas.api.model.AuthorQuotesCount;
import com.xavelo.filocitas.api.model.Quote;
import com.xavelo.filocitas.api.model.QuoteRequest;
import com.xavelo.filocitas.port.in.DeleteQuoteUseCase;
import com.xavelo.filocitas.port.in.SaveUquoteUseCase;
import com.xavelo.filocitas.port.in.GetAuthorsQuotesCountUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AdminController implements AdminApi {

    private static final Logger logger = LogManager.getLogger(AdminController.class);

    private final SaveUquoteUseCase saveUquoteUseCase;
    private final DeleteQuoteUseCase deleteQuoteUseCase;
    private final GetAuthorsQuotesCountUseCase getAuthorsQuotesCountUseCase;
    private final ApiMapper apiMapper;

    public AdminController(SaveUquoteUseCase saveUquoteUseCase,
                           DeleteQuoteUseCase deleteQuoteUseCase,
                           GetAuthorsQuotesCountUseCase getAuthorsQuotesCountUseCase,
                           ApiMapper apiMapper) {
        this.saveUquoteUseCase = saveUquoteUseCase;
        this.deleteQuoteUseCase = deleteQuoteUseCase;
        this.getAuthorsQuotesCountUseCase = getAuthorsQuotesCountUseCase;
        this.apiMapper = apiMapper;
    }

    @Override
    public ResponseEntity<Quote> createQuote(@Valid @RequestBody QuoteRequest quoteRequest) {
        var domainQuote = apiMapper.toDomainQuote(quoteRequest);
        logger.info("Received quote '{}' from author {}", domainQuote.getQuote(), domainQuote.getAuthor().getName());
        var savedQuote = saveUquoteUseCase.saveQuote(domainQuote);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiMapper.toApiQuote(savedQuote));
    }

    @Override
    public ResponseEntity<List<Quote>> createQuotes(@Valid @RequestBody @Size(min = 1) List<@Valid QuoteRequest> quoteRequest) {
        var domainQuotes = apiMapper.toDomainQuotes(quoteRequest);
        domainQuotes.forEach(quote -> logger.info("Received quote '{}' from author {}", quote.getQuote(), quote.getAuthor().getName()));
        var savedQuotes = saveUquoteUseCase.saveQuotes(domainQuotes);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiMapper.toApiQuotes(savedQuotes));
    }

    @Override
    public ResponseEntity<Void> deleteQuote(@PathVariable("id") UUID id) {
        logger.info("Deleting quote with id {}", id);
        deleteQuoteUseCase.deleteQuote(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AuthorQuotesCount>> getAuthorsQuotesCountLimit(@PathVariable("limit") Integer limit) {
        logger.info("Fetching top {} authors with quote counts", limit);
        if (limit == null || limit < 1) {
            logger.warn("Requested author quote count limit {} is invalid", limit);
            return ResponseEntity.badRequest().build();
        }

        List<AuthorQuotesCount> authors = apiMapper.toApiAuthorQuotesCounts(
                getAuthorsQuotesCountUseCase.getTopAuthorsQuotesCount(limit)
        );
        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }
}
