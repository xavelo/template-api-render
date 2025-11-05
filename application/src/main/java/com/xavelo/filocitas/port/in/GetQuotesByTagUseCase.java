package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.quote.Quote;

import java.util.List;

public interface GetQuotesByTagUseCase {

    List<Quote> getQuotesByTag(String tagName);
}
