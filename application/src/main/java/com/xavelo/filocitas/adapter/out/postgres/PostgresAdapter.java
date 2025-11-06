package com.xavelo.filocitas.adapter.out.postgres;

import com.xavelo.filocitas.adapter.out.postgres.mapper.QuoteMapper;
import com.xavelo.filocitas.adapter.out.postgres.repository.QuoteRepository;
import com.xavelo.filocitas.application.domain.quote.Quote;
import com.xavelo.filocitas.adapter.out.postgres.repository.TagRepository;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.QuoteEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.TagEntity;
import com.xavelo.filocitas.application.domain.tag.Tag;
import com.xavelo.filocitas.port.out.DeleteQuotePort;
import com.xavelo.filocitas.port.out.LoadQuotePort;
import com.xavelo.filocitas.port.out.SaveQuotePort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.UUID;

@Repository
public class PostgresAdapter implements SaveQuotePort, LoadQuotePort, DeleteQuotePort {

    private final QuoteRepository quoteRepository;
    private final QuoteMapper quoteMapper;
    private final TagRepository tagRepository;

    public PostgresAdapter(QuoteRepository quoteRepository, QuoteMapper quoteMapper, TagRepository tagRepository) {
        this.quoteRepository = quoteRepository;
        this.quoteMapper = quoteMapper;
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public Quote saveQuote(Quote quote) {
        var tagEntities = resolveTagEntities(quote.getTags());
        var quoteEntity = quoteMapper.toEntity(quote, tagEntities);
        var savedQuoteEntity = quoteRepository.save(quoteEntity);
        return quoteMapper.toDomain(savedQuoteEntity);
    }

    @Override
    @Transactional
    public List<Quote> saveQuotes(List<Quote> quotes) {
        if (quotes == null || quotes.isEmpty()) {
            return List.of();
        }

        var quoteEntities = new ArrayList<QuoteEntity>(quotes.size());
        for (Quote quote : quotes) {
            var tagEntities = resolveTagEntities(quote.getTags());
            var quoteEntity = quoteMapper.toEntity(quote, tagEntities);
            quoteEntities.add(quoteEntity);
        }

        var savedQuoteEntities = quoteRepository.saveAll(quoteEntities);
        return savedQuoteEntities.stream()
                .map(quoteMapper::toDomain)
                .collect(Collectors.toList());
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
        return tagRepository.findAllTagNames();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quote> findQuotesByTagName(String tagName) {
        return quoteRepository.findAllByTags_Name(tagName).stream()
                .map(quoteMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteQuoteById(UUID id) {
        quoteRepository.deleteById(id);
    }

    private Set<TagEntity> resolveTagEntities(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return Set.of();
        }

        var uniqueTags = tags.stream()
                .filter(tag -> tag != null && (tag.getId() != null || (tag.getName() != null && !tag.getName().isBlank())))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        var ids = uniqueTags.stream()
                .map(Tag::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        var tagsById = ids.isEmpty()
                ? new LinkedHashMap<UUID, TagEntity>()
                : tagRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(TagEntity::getId, tag -> tag, (left, right) -> left, LinkedHashMap::new));

        var names = uniqueTags.stream()
                .map(Tag::getName)
                .filter(name -> name != null && !name.isBlank())
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        var tagsByName = names.isEmpty()
                ? new LinkedHashMap<String, TagEntity>()
                : tagRepository.findAllByNameIn(names).stream()
                .collect(Collectors.toMap(TagEntity::getName, tag -> tag, (left, right) -> left, LinkedHashMap::new));

        var resolvedTags = new LinkedHashSet<TagEntity>();
        for (Tag tag : uniqueTags) {
            if (tag == null) {
                continue;
            }
            TagEntity entity = null;
            if (tag.getId() != null) {
                entity = tagsById.get(tag.getId());
            }
            if (entity == null) {
                var name = tag.getName();
                if (name != null && !name.isBlank()) {
                    name = name.trim();
                    if (name.isEmpty()) {
                        continue;
                    }
                    entity = tagsByName.get(name);
                    if (entity == null) {
                        entity = tagRepository.save(TagEntity.newInstance(name));
                        tagsByName.put(name, entity);
                    }
                }
            }
            if (entity != null) {
                resolvedTags.add(entity);
            }
        }
        return resolvedTags;
    }
}
