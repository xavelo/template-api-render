package com.xavelo.filocitas.adapter.out.postgres;

import com.xavelo.filocitas.adapter.out.postgres.mapper.QuoteMapper;
import com.xavelo.filocitas.adapter.out.postgres.repository.QuoteRepository;
import com.xavelo.filocitas.application.domain.quote.Quote;
import com.xavelo.filocitas.port.out.DeleteQuotePort;
import com.xavelo.filocitas.port.out.LoadQuotePort;
import com.xavelo.filocitas.port.out.SaveQuotePort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.UUID;

@Repository
public class PostgresAdapter implements SaveQuotePort, LoadQuotePort, DeleteQuotePort {

    private final QuoteRepository quoteRepository;
    private final QuoteMapper quoteMapper;

    public PostgresAdapter(QuoteRepository quoteRepository, QuoteMapper quoteMapper) {
        this.quoteRepository = quoteRepository;
        this.quoteMapper = quoteMapper;
    }

    @Override
    @Transactional
    public Quote saveQuote(Quote quote) {
        var quoteEntity = quoteMapper.toEntity(quote);
        var savedQuoteEntity = quoteRepository.save(quoteEntity);
        return quoteMapper.toDomain(savedQuoteEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Quote> findQuoteById(UUID id) {
        return quoteRepository.findById(id).map(quoteMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Quote> findRandomQuote() {
        return quoteRepository.findRandomQuote().map(quoteMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public long countQuotes() {
        return quoteRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quote> findQuotesByAuthorId(UUID authorId) {
        return quoteRepository.findAllByAuthorId(authorId).stream()
                .map(quoteMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAllTags() {
        return quoteRepository.findDistinctThemeTags();
    }

    @Override
    @Transactional
    public void deleteQuoteById(UUID id) {
        quoteRepository.deleteById(id);
    }
}
