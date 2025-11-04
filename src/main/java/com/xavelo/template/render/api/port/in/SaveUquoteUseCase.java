package com.xavelo.template.render.api.port.in;

import com.xavelo.template.render.api.application.domain.quote.Quote;

public interface SaveUquoteUseCase {

    Quote saveQuote(Quote quote);
}
