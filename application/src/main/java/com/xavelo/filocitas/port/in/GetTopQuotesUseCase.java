package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.quote.QuoteWithLikes;

import java.util.List;

public interface GetTopQuotesUseCase {

    List<QuoteWithLikes> getTopQuotes(int limit);
}
