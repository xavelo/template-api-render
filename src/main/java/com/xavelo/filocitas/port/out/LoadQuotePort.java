package com.xavelo.filocitas.port.out;

import com.xavelo.filocitas.application.domain.quote.Quote;

import java.util.Optional;
import java.util.UUID;

public interface LoadQuotePort {

    Optional<Quote> findQuoteById(UUID id);

    Optional<Quote> findRandomQuote();

    long countQuotes();
}
