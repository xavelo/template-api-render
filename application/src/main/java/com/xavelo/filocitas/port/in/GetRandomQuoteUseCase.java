package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.Quote;

import java.util.Optional;

public interface GetRandomQuoteUseCase {

    Optional<Quote> getRandomQuote();
}
