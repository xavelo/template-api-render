package com.xavelo.template.render.api.port.out;

import com.xavelo.template.render.api.application.domain.quote.Quote;

public interface SaveQuotePort {

    void saveQuote(Quote quote);
}
