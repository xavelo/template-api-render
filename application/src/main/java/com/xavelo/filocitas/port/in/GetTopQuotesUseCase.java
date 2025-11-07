package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.Quote;

import java.util.List;

public interface GetTopQuotesUseCase {

    List<Quote> getTopQuotes(int limit);
}
