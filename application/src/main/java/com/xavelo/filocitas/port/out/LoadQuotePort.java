package com.xavelo.filocitas.port.out;

import com.xavelo.filocitas.application.domain.quote.Quote;
import com.xavelo.filocitas.application.domain.quote.QuoteWithLikes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadQuotePort {

    Optional<Quote> findQuoteById(UUID id);

    Optional<Quote> findRandomQuote();

    long countQuotes();

    List<Quote> findQuotesByAuthorId(UUID authorId);

    List<String> findAllTags();

    List<Quote> findQuotesByTagName(String tagName);

    List<QuoteWithLikes> findTopQuotesByLikes(int limit);
}
