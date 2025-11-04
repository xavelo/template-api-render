package com.xavelo.filocitas.adapter.out.postgres;

import com.xavelo.filocitas.adapter.out.postgres.mapper.QuoteMapper;
import com.xavelo.filocitas.adapter.out.postgres.repository.QuoteRepository;
import com.xavelo.filocitas.application.domain.quote.Quote;
import com.xavelo.filocitas.port.out.LoadQuotePort;
import com.xavelo.filocitas.port.out.SaveQuotePort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PostgresAdapter implements SaveQuotePort, LoadQuotePort {

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
}
