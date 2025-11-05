package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.quote.Quote;

public interface SaveUquoteUseCase {

    Quote saveQuote(Quote quote);
}
