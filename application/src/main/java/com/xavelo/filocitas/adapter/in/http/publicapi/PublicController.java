package com.xavelo.filocitas.adapter.in.http.publicapi;

import com.xavelo.filocitas.adapter.in.http.mapper.ApiMapper;
import com.xavelo.filocitas.api.PublicApi;
import com.xavelo.filocitas.api.model.Author;
import com.xavelo.filocitas.api.model.Quote;
import com.xavelo.filocitas.api.model.QuoteLikesResponse;
import com.xavelo.filocitas.api.model.QuoteWithLikes;
import com.xavelo.filocitas.port.in.GetAllAuthorsUseCase;
import com.xavelo.filocitas.port.in.GetAllTagsUseCase;
import com.xavelo.filocitas.port.in.GetAuthorByIdUseCase;
import com.xavelo.filocitas.port.in.GetQuoteByIdUseCase;
import com.xavelo.filocitas.port.in.GetQuotesByAuthorIdUseCase;
import com.xavelo.filocitas.port.in.GetQuotesByTagUseCase;
import com.xavelo.filocitas.port.in.GetRandomQuoteUseCase;
import com.xavelo.filocitas.port.in.GetQuoteLikesUseCase;
import com.xavelo.filocitas.port.in.GetTopQuotesUseCase;
import com.xavelo.filocitas.port.in.LikeQuoteUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PublicController implements PublicApi {

    private static final Logger logger = LogManager.getLogger(PublicController.class);

    private final GetQuoteByIdUseCase getQuoteByIdUseCase;
    private final GetAuthorByIdUseCase getAuthorByIdUseCase;
    private final GetAllAuthorsUseCase getAllAuthorsUseCase;
    private final GetRandomQuoteUseCase getRandomQuoteUseCase;
    private final GetQuotesByAuthorIdUseCase getQuotesByAuthorIdUseCase;
    private final GetAllTagsUseCase getAllTagsUseCase;
    private final GetQuotesByTagUseCase getQuotesByTagUseCase;
    private final ApiMapper apiMapper;
    private final LikeQuoteUseCase likeQuoteUseCase;
    private final GetQuoteLikesUseCase getQuoteLikesUseCase;
    private final GetTopQuotesUseCase getTopQuotesUseCase;

    private static final Set<Integer> ALLOWED_TOP_LIMITS = Set.of(3, 5, 10, 25, 50);

    public PublicController(GetQuoteByIdUseCase getQuoteByIdUseCase,
                            GetAuthorByIdUseCase getAuthorByIdUseCase,
                            GetAllAuthorsUseCase getAllAuthorsUseCase,
                            GetRandomQuoteUseCase getRandomQuoteUseCase,
                            GetQuotesByAuthorIdUseCase getQuotesByAuthorIdUseCase,
                            GetAllTagsUseCase getAllTagsUseCase,
                            GetQuotesByTagUseCase getQuotesByTagUseCase,
                            ApiMapper apiMapper,
                            LikeQuoteUseCase likeQuoteUseCase,
                            GetQuoteLikesUseCase getQuoteLikesUseCase,
                            GetTopQuotesUseCase getTopQuotesUseCase) {
        this.getQuoteByIdUseCase = getQuoteByIdUseCase;
        this.getAuthorByIdUseCase = getAuthorByIdUseCase;
        this.getAllAuthorsUseCase = getAllAuthorsUseCase;
        this.getRandomQuoteUseCase = getRandomQuoteUseCase;
        this.getQuotesByAuthorIdUseCase = getQuotesByAuthorIdUseCase;
        this.getAllTagsUseCase = getAllTagsUseCase;
        this.getQuotesByTagUseCase = getQuotesByTagUseCase;
        this.apiMapper = apiMapper;
        this.likeQuoteUseCase = likeQuoteUseCase;
        this.getQuoteLikesUseCase = getQuoteLikesUseCase;
        this.getTopQuotesUseCase = getTopQuotesUseCase;
    }

    @Override
    public ResponseEntity<List<Author>> getAuthors() {
        logger.info("Fetching all authors");
        List<Author> authors = apiMapper.toApiAuthors(getAllAuthorsUseCase.getAuthors());
        if (authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }

    @Override
    public ResponseEntity<Quote> getQuoteById(@PathVariable("id") UUID id) {
        logger.info("Fetching quote with id {}", id);
        return getQuoteByIdUseCase.getQuoteById(id)
                .map(quote -> ResponseEntity.ok(apiMapper.toApiQuote(quote)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Quote> getRandomQuote() {
        logger.info("Fetching random quote");
        return getRandomQuoteUseCase.getRandomQuote()
                .map(quote -> ResponseEntity.ok(apiMapper.toApiQuote(quote)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Override
    public ResponseEntity<Author> getAuthorById(@PathVariable("id") UUID id) {
        logger.info("Fetching author with id {}", id);
        return getAuthorByIdUseCase.getAuthorById(id)
                .map(author -> ResponseEntity.ok(apiMapper.toApiAuthor(author)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Quote>> getQuotesByAuthor(@PathVariable("id") UUID authorId) {
        logger.info("Fetching quotes for author with id {}", authorId);
        List<Quote> quotes = apiMapper.toApiQuotes(getQuotesByAuthorIdUseCase.getQuotesByAuthorId(authorId));
        if (quotes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(quotes);
    }

    @Override
    public ResponseEntity<List<String>> getTags() {
        logger.info("Fetching all tags");
        List<String> tags = getAllTagsUseCase.getTags();
        if (tags.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tags);
    }

    @Override
    public ResponseEntity<List<Quote>> getQuotesByTag(@PathVariable("name") String name) {
        logger.info("Fetching quotes for tag {}", name);
        List<Quote> quotes = apiMapper.toApiQuotes(getQuotesByTagUseCase.getQuotesByTag(name));
        if (quotes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(quotes);
    }

    @Override
    public ResponseEntity<QuoteLikesResponse> likeQuote(@PathVariable("id") UUID id) {
        logger.info("Incrementing likes for quote with id {}", id);
        return likeQuoteUseCase.likeQuote(id)
                .map(likes -> ResponseEntity.ok(apiMapper.toQuoteLikesResponse(likes)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<QuoteLikesResponse> getQuoteLikes(@PathVariable("id") UUID id) {
        logger.info("Fetching likes for quote with id {}", id);
        return getQuoteLikesUseCase.getQuoteLikes(id)
                .map(likes -> ResponseEntity.ok(apiMapper.toQuoteLikesResponse(likes)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<QuoteWithLikes>> getTopQuotes(@PathVariable("limit") Integer limit) {
        logger.info("Fetching top {} quotes by likes", limit);
        if (limit == null || !ALLOWED_TOP_LIMITS.contains(limit)) {
            logger.warn("Requested top quote limit {} is not supported", limit);
            return ResponseEntity.badRequest().build();
        }

        var quotes = apiMapper.toApiQuoteWithLikes(getTopQuotesUseCase.getTopQuotes(limit));
        if (quotes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(quotes);
    }
}
