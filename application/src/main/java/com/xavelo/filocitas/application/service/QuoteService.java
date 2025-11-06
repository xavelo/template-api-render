package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.quote.Quote;
import com.xavelo.filocitas.port.in.DeleteQuoteUseCase;
import com.xavelo.filocitas.port.in.GetAllTagsUseCase;
import com.xavelo.filocitas.port.in.GetQuoteByIdUseCase;
import com.xavelo.filocitas.port.in.GetQuotesByAuthorIdUseCase;
import com.xavelo.filocitas.port.in.GetQuotesByTagUseCase;
import com.xavelo.filocitas.port.in.GetQuotesCountUseCase;
import com.xavelo.filocitas.port.in.GetRandomQuoteUseCase;
import com.xavelo.filocitas.port.in.SaveUquoteUseCase;
import com.xavelo.filocitas.port.out.LoadQuotePort;
import com.xavelo.filocitas.port.out.SaveQuotePort;
import com.xavelo.filocitas.port.out.DeleteQuotePort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuoteService implements SaveUquoteUseCase,
        DeleteQuoteUseCase,
        GetQuoteByIdUseCase,
        GetRandomQuoteUseCase,
        GetQuotesCountUseCase,
        GetQuotesByAuthorIdUseCase,
        GetQuotesByTagUseCase,
        GetAllTagsUseCase {

    private final SaveQuotePort saveQuotePort;
    private final LoadQuotePort loadQuotePort;
    private final DeleteQuotePort deleteQuotePort;

    public QuoteService(SaveQuotePort saveQuotePort, LoadQuotePort loadQuotePort, DeleteQuotePort deleteQuotePort) {
        this.saveQuotePort = saveQuotePort;
        this.loadQuotePort = loadQuotePort;
        this.deleteQuotePort = deleteQuotePort;
    }

    @Override
    public Quote saveQuote(Quote quote) {
        return saveQuotePort.saveQuote(quote);
    }

    @Override
    public List<Quote> saveQuotes(List<Quote> quotes) {
        if (quotes == null || quotes.isEmpty()) {
            return List.of();
        }
        return saveQuotePort.saveQuotes(quotes);
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

    @Override
    public List<String> getTags() {
        return loadQuotePort.findAllTags();
    }

    @Override
    public List<Quote> getQuotesByTag(String tagName) {
        return loadQuotePort.findQuotesByTagName(tagName);
    }

    @Override
    public void deleteQuote(UUID id) {
        deleteQuotePort.deleteQuoteById(id);
    }
}
