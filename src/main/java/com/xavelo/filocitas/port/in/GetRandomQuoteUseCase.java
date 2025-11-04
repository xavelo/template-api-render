package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.quote.Quote;

import java.util.Optional;

public interface GetRandomQuoteUseCase {

    Optional<Quote> getRandomQuote();
}
