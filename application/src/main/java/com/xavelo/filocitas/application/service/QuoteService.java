package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.Quote;
import com.xavelo.filocitas.application.domain.Tag;
import com.xavelo.filocitas.port.in.DeleteQuoteUseCase;
import com.xavelo.filocitas.port.in.GetAllTagsUseCase;
import com.xavelo.filocitas.port.in.GetQuoteLikesUseCase;
import com.xavelo.filocitas.port.in.LikeQuoteUseCase;
import com.xavelo.filocitas.port.in.ExportQuotesUseCase;
import com.xavelo.filocitas.port.in.GetQuoteByIdUseCase;
import com.xavelo.filocitas.port.in.GetQuotesByAuthorIdUseCase;
import com.xavelo.filocitas.port.in.GetQuotesByTagUseCase;
import com.xavelo.filocitas.port.in.GetQuotesCountUseCase;
import com.xavelo.filocitas.port.in.GetTopQuotesUseCase;
import com.xavelo.filocitas.port.in.GetRandomQuoteUseCase;
import com.xavelo.filocitas.port.in.SaveUquoteUseCase;
import com.xavelo.filocitas.port.out.LoadQuotePort;
import com.xavelo.filocitas.port.out.LikeQuotePort;
import com.xavelo.filocitas.port.out.SaveQuotePort;
import com.xavelo.filocitas.port.out.DeleteQuotePort;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        ExportQuotesUseCase,
        GetTopQuotesUseCase {

    private final SaveQuotePort saveQuotePort;
    private final LoadQuotePort loadQuotePort;
    private final DeleteQuotePort deleteQuotePort;
    private final LikeQuotePort likeQuotePort;
    private final TagService tagService;

    public QuoteService(SaveQuotePort saveQuotePort,
                        LoadQuotePort loadQuotePort,
                        DeleteQuotePort deleteQuotePort,
                        LikeQuotePort likeQuotePort,
                        TagService tagService) {
        this.saveQuotePort = saveQuotePort;
        this.loadQuotePort = loadQuotePort;
        this.deleteQuotePort = deleteQuotePort;
        this.likeQuotePort = likeQuotePort;
        this.tagService = tagService;
    }

    @Override
    public Quote saveQuote(Quote quote) {
        var tags = tagService.checkTags(quote.getTags());
        return saveQuotePort.saveQuote(quote.withTags(tags));
    }

    @Override
    public List<Quote> saveQuotes(List<Quote> quotes) {
        if (quotes == null || quotes.isEmpty()) {
            return List.of();
        }
        var preparedQuotes = quotes.stream()
                .map(tagService::ensureTags)
                .collect(Collectors.toList());
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
    public List<Quote> exportQuotes() {
        return loadQuotePort.findAllQuotes();
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
                .map(quote -> likeQuotePort.incrementQuoteLike(quoteId));
    }

    @Override
    public Optional<Long> getQuoteLikes(UUID quoteId) {
        return loadQuotePort.findQuoteById(quoteId)
                .map(Quote::getLikes);
    }

    @Override
    public List<Quote> getTopQuotes(int limit) {
        if (limit <= 0) {
            return List.of();
        }
        return loadQuotePort.findTopQuotesByLikes(limit);
    }

    private List<Tag> resolveQuoteTags(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }

        var resolved = new LinkedHashMap<String, Tag>();
        for (Tag tag : tags) {
            if (tag == null) {
                continue;
            }
            var normalizedName = normalizeTagName(tag.getName());
            if (normalizedName == null) {
                continue;
            }
            var resolvedTag = tagService.checkTag(normalizedName);
            if (resolvedTag == null) {
                continue;
            }
            var key = resolvedTag.getId() != null ? resolvedTag.getId().toString() : normalizedName;
            if (key != null) {
                resolved.putIfAbsent(key, resolvedTag);
            }
        }

        return resolved.isEmpty() ? List.of() : List.copyOf(resolved.values());
    }

    private String normalizeTagName(String name) {
        if (name == null) {
            return null;
        }
        var trimmed = name.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
