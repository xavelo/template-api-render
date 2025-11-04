package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.quote.Quote;

import java.util.List;
import java.util.UUID;

public interface GetQuotesByAuthorIdUseCase {

    List<Quote> getQuotesByAuthorId(UUID authorId);
}
