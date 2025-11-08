package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.Quote;

import java.util.List;

public interface SaveUquoteUseCase {

    Quote saveQuote(Quote quote, String rawPayload);

    List<Quote> saveQuotes(List<Quote> quotes, List<String> rawPayloads);
}
