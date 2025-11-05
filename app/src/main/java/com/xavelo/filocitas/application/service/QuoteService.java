package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.quote.Quote;
import com.xavelo.filocitas.port.in.GetQuoteByIdUseCase;
import com.xavelo.filocitas.port.in.GetQuotesByAuthorIdUseCase;
import com.xavelo.filocitas.port.in.GetQuotesCountUseCase;
import com.xavelo.filocitas.port.in.GetRandomQuoteUseCase;
import com.xavelo.filocitas.port.in.SaveUquoteUseCase;
import com.xavelo.filocitas.port.out.LoadQuotePort;
import com.xavelo.filocitas.port.out.SaveQuotePort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuoteService implements SaveUquoteUseCase,
        GetQuoteByIdUseCase,
        GetRandomQuoteUseCase,
        GetQuotesCountUseCase,
        GetQuotesByAuthorIdUseCase {

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

    @Override
    public long getQuotesCount() {
        return loadQuotePort.countQuotes();
    }

    @Override
    public List<Quote> getQuotesByAuthorId(UUID authorId) {
        return loadQuotePort.findQuotesByAuthorId(authorId);
    }
}
