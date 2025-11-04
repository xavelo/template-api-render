package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.quote.Quote;
import com.xavelo.filocitas.port.in.GetQuoteByIdUseCase;
import com.xavelo.filocitas.port.in.GetRandomQuoteUseCase;
import com.xavelo.filocitas.port.in.SaveUquoteUseCase;
import com.xavelo.filocitas.port.out.LoadQuotePort;
import com.xavelo.filocitas.port.out.SaveQuotePort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class QuoteService implements SaveUquoteUseCase, GetQuoteByIdUseCase, GetRandomQuoteUseCase {

    private final SaveQuotePort saveQuotePort;
    private final LoadQuotePort loadQuotePort;

    public QuoteService(SaveQuotePort saveQuotePort, LoadQuotePort loadQuotePort) {
        this.saveQuotePort = saveQuotePort;
        this.loadQuotePort = loadQuotePort;
    }

    @Override
    public Quote saveQuote(Quote quote) {
        return saveQuotePort.saveQuote(quote);
    }

    @Override
    public Optional<Quote> getQuoteById(UUID id) {
        return loadQuotePort.findQuoteById(id);
    }

    @Override
    public Optional<Quote> getRandomQuote() {
        return loadQuotePort.findRandomQuote();
    }
}
