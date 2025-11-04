package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.quote.Quote;
import com.xavelo.filocitas.port.in.SaveUquoteUseCase;
import com.xavelo.filocitas.port.out.SaveQuotePort;
import org.springframework.stereotype.Service;

@Service
public class QuoteService implements SaveUquoteUseCase {

    private final SaveQuotePort saveQuotePort;

    public QuoteService(SaveQuotePort saveQuotePort) {
        this.saveQuotePort = saveQuotePort;
    }

    @Override
    public Quote saveQuote(Quote quote) {
        return saveQuotePort.saveQuote(quote);
    }
}
