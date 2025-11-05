package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.quote.Quote;

import java.util.Optional;
import java.util.UUID;

public interface GetQuoteByIdUseCase {

    Optional<Quote> getQuoteById(UUID id);
}
