package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.Quote;

import java.util.List;

public interface SaveUquoteUseCase {

    Quote saveQuote(Quote quote);

    List<Quote> saveQuotes(List<Quote> quotes);
}
