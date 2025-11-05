package com.xavelo.filocitas.port.out;

import com.xavelo.filocitas.application.domain.quote.Quote;

public interface SaveQuotePort {

    Quote saveQuote(Quote quote);
}
