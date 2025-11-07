package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.Quote;
import com.xavelo.filocitas.application.domain.QuoteWithLikes;
import com.xavelo.filocitas.port.in.DeleteQuoteUseCase;
import com.xavelo.filocitas.port.in.GetAllTagsUseCase;
import com.xavelo.filocitas.port.in.GetQuoteLikesUseCase;
import com.xavelo.filocitas.port.in.LikeQuoteUseCase;
import com.xavelo.filocitas.port.in.GetQuoteByIdUseCase;
import com.xavelo.filocitas.port.in.GetQuotesByAuthorIdUseCase;
import com.xavelo.filocitas.port.in.GetQuotesByTagUseCase;
import com.xavelo.filocitas.port.in.GetQuotesCountUseCase;
import com.xavelo.filocitas.port.in.GetTopQuotesUseCase;
import com.xavelo.filocitas.port.in.GetRandomQuoteUseCase;
import com.xavelo.filocitas.port.in.SaveUquoteUseCase;
import com.xavelo.filocitas.port.out.LoadQuotePort;
import com.xavelo.filocitas.port.out.IncrementQuoteLikePort;
import com.xavelo.filocitas.port.out.SaveQuotePort;
import com.xavelo.filocitas.port.out.LoadQuoteLikePort;
import com.xavelo.filocitas.port.out.DeleteQuotePort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QuoteService implements SaveUquoteUseCase,
        DeleteQuoteUseCase,
        GetQuoteByIdUseCase,
        GetRandomQuoteUseCase,
        GetQuotesCountUseCase,
        GetQuotesByAuthorIdUseCase,
        GetQuotesByTagUseCase,
        GetAllTagsUseCase,
        LikeQuoteUseCase,
        GetQuoteLikesUseCase,
        GetTopQuotesUseCase {

    private final SaveQuotePort saveQuotePort;
    private final LoadQuotePort loadQuotePort;
    private final DeleteQuotePort deleteQuotePort;
    private final IncrementQuoteLikePort incrementQuoteLikePort;
    private final LoadQuoteLikePort loadQuoteLikePort;
    private final TagService tagService;

    public QuoteService(SaveQuotePort saveQuotePort,
                        LoadQuotePort loadQuotePort,
                        DeleteQuotePort deleteQuotePort,
                        IncrementQuoteLikePort incrementQuoteLikePort,
                        LoadQuoteLikePort loadQuoteLikePort,
                        TagService tagService) {
        this.saveQuotePort = saveQuotePort;
        this.loadQuotePort = loadQuotePort;
        this.deleteQuotePort = deleteQuotePort;
        this.incrementQuoteLikePort = incrementQuoteLikePort;
        this.loadQuoteLikePort = loadQuoteLikePort;
        this.tagService = tagService;
    }

    @Override
    public Quote saveQuote(Quote quote) {
        return saveQuotePort.saveQuote(tagService.ensureTags(quote));
    }

    @Override
    public List<Quote> saveQuotes(List<Quote> quotes) {
        if (quotes == null || quotes.isEmpty()) {
            return List.of();
        }
        var preparedQuotes = quotes.stream()
                .filter(Objects::nonNull)
                .map(tagService::ensureTags)
                .collect(Collectors.toList());

        if (preparedQuotes.isEmpty()) {
            return List.of();
        }

        return saveQuotePort.saveQuotes(preparedQuotes);
    }

    @Override
    public Optional<Quote> getQuoteById(UUID id) {
        return loadQuotePort.findQuoteById(id);
    }

    @Override
    public Optional<Quote> getRandomQuote() {
        return loadQuotePort.findRandomQuote();
    }

    @Override
    public long getQuotesCount() {
        return loadQuotePort.countQuotes();
    }

    @Override
    public List<Quote> getQuotesByAuthorId(UUID authorId) {
        return loadQuotePort.findQuotesByAuthorId(authorId);
    }

    @Override
    public List<String> getTags() {
        return loadQuotePort.findAllTags();
    }

    @Override
    public List<Quote> getQuotesByTag(String tagName) {
        return loadQuotePort.findQuotesByTagName(tagName);
    }

    @Override
    public void deleteQuote(UUID id) {
        deleteQuotePort.deleteQuoteById(id);
    }

    @Override
    public Optional<Long> likeQuote(UUID quoteId) {
        return loadQuotePort.findQuoteById(quoteId)
                .map(quote -> incrementQuoteLikePort.incrementQuoteLike(quoteId));
    }

    @Override
    public Optional<Long> getQuoteLikes(UUID quoteId) {
        return loadQuotePort.findQuoteById(quoteId)
                .map(quote -> loadQuoteLikePort.getQuoteLikes(quoteId));
    }

    @Override
    public List<QuoteWithLikes> getTopQuotes(int limit) {
        if (limit <= 0) {
            return List.of();
        }
        return loadQuotePort.findTopQuotesByLikes(limit);
    }
}
