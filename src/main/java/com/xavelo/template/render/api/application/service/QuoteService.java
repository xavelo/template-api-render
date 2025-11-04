package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.domain.quote.Quote;
import com.xavelo.template.render.api.port.in.SaveUquoteUseCase;
import com.xavelo.template.render.api.port.out.SaveQuotePort;
import org.springframework.stereotype.Service;

@Service
public class QuoteService implements SaveUquoteUseCase {

    private final SaveQuotePort saveQuotePort;

    public QuoteService(SaveQuotePort saveQuotePort) {
        this.saveQuotePort = saveQuotePort;
    }

    @Override
    public Quote saveQuote(Quote quote) {
        saveQuotePort.saveQuote(quote);
        return quote;
    }
}
