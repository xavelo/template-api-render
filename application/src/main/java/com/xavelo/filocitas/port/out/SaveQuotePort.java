package com.xavelo.filocitas.port.out;

import com.xavelo.filocitas.application.domain.Quote;

import java.util.List;

public interface SaveQuotePort {

    Quote saveQuote(Quote quote);

    List<Quote> saveQuotes(List<Quote> quotes);
}
