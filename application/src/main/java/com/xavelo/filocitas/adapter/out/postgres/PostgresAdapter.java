package com.xavelo.filocitas.adapter.out.postgres;

import com.xavelo.filocitas.adapter.out.postgres.mapper.QuoteMapper;
import com.xavelo.filocitas.adapter.out.postgres.repository.QuoteRepository;
import com.xavelo.filocitas.adapter.out.postgres.repository.TagRepository;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.TagEntity;
import com.xavelo.filocitas.application.domain.quote.Quote;
import com.xavelo.filocitas.port.out.DeleteQuotePort;
import com.xavelo.filocitas.port.out.LoadQuotePort;
import com.xavelo.filocitas.port.out.SaveQuotePort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.UUID;

@Repository
public class PostgresAdapter implements SaveQuotePort, LoadQuotePort, DeleteQuotePort {

    private final QuoteRepository quoteRepository;
    private final TagRepository tagRepository;
    private final QuoteMapper quoteMapper;

    public PostgresAdapter(QuoteRepository quoteRepository, TagRepository tagRepository, QuoteMapper quoteMapper) {
        this.quoteRepository = quoteRepository;
        this.tagRepository = tagRepository;
        this.quoteMapper = quoteMapper;
    }

    @Override
    @Transactional
    public Quote saveQuote(Quote quote) {
        var tagEntities = upsertTags(quote.getTags());
        var quoteEntity = quoteMapper.toEntity(quote, tagEntities);
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
        return tagRepository.findAllByOrderByNameAsc().stream()
                .map(TagEntity::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteQuoteById(UUID id) {
        quoteRepository.deleteById(id);
    }

    private List<TagEntity> upsertTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }

        Map<String, TagEntity> uniqueTags = new LinkedHashMap<>();
        for (String tag : tags) {
            if (tag == null) {
                continue;
            }
            String normalizedTag = tag.trim();
            if (normalizedTag.isEmpty()) {
                continue;
            }
            uniqueTags.computeIfAbsent(normalizedTag, this::findOrCreateTag);
        }
        return new ArrayList<>(uniqueTags.values());
    }

    private TagEntity findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(TagEntity.newInstance(tagName)));
    }
}
