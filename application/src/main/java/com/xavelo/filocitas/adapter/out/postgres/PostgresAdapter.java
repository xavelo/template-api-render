package com.xavelo.filocitas.adapter.out.postgres;

import com.xavelo.filocitas.adapter.out.postgres.mapper.AuthorMapper;
import com.xavelo.filocitas.adapter.out.postgres.mapper.QuoteMapper;
import com.xavelo.filocitas.adapter.out.postgres.mapper.TagMapper;
import com.xavelo.filocitas.adapter.out.postgres.repository.AuthorRepository;
import com.xavelo.filocitas.adapter.out.postgres.repository.QuoteRepository;
import com.xavelo.filocitas.adapter.out.postgres.repository.RawQuoteRepository;
import com.xavelo.filocitas.adapter.out.postgres.repository.TagRepository;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.AuthorEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.QuoteEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.RawQuoteEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.TagEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.projection.AuthorQuoteCountProjection;
import com.xavelo.filocitas.application.domain.Author;
import com.xavelo.filocitas.application.domain.AuthorQuotesSummary;
import com.xavelo.filocitas.application.domain.Quote;
import com.xavelo.filocitas.application.domain.Tag;
import com.xavelo.filocitas.application.exception.DuplicatedQuoteException;
import com.xavelo.filocitas.port.out.DeleteQuotePort;
import com.xavelo.filocitas.port.out.LikeQuotePort;
import com.xavelo.filocitas.port.out.LoadAuthorPort;
import com.xavelo.filocitas.port.out.LoadQuotePort;
import com.xavelo.filocitas.port.out.SaveQuotePort;
import com.xavelo.filocitas.port.out.SaveTagPort;
import com.xavelo.filocitas.port.out.LoadTagPort;
import com.xavelo.filocitas.port.out.SaveRawQuotePort;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class PostgresAdapter implements SaveQuotePort,
        SaveRawQuotePort,
        LoadQuotePort,
        DeleteQuotePort,
        LoadAuthorPort,
        LikeQuotePort,
        SaveTagPort,
        LoadTagPort {

    private final QuoteRepository quoteRepository;
    private final QuoteMapper quoteMapper;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final RawQuoteRepository rawQuoteRepository;

    public PostgresAdapter(QuoteRepository quoteRepository,
                           QuoteMapper quoteMapper,
                           TagRepository tagRepository,
                           TagMapper tagMapper,
                           AuthorRepository authorRepository,
                           AuthorMapper authorMapper,
                           RawQuoteRepository rawQuoteRepository) {
        this.quoteRepository = quoteRepository;
        this.quoteMapper = quoteMapper;
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.rawQuoteRepository = rawQuoteRepository;
    }

    @Override
    @Transactional
    public Quote saveQuote(Quote quote) {
        var tagEntities = loadTagEntities(quote.getTags());
        var authorEntity = resolveAuthorEntity(quote.getAuthor(), new LinkedHashMap<>(), new LinkedHashMap<>());
        try {
            var quoteEntity = quoteMapper.toEntity(quote, authorEntity, tagEntities);
            var savedQuoteEntity = quoteRepository.save(quoteEntity);
            return quoteMapper.toDomain(savedQuoteEntity);
        } catch (DataIntegrityViolationException exception) {
            if (isDuplicateQuoteViolation(exception)) {
                throw new DuplicatedQuoteException(quote == null ? null : quote.getQuote(), exception);
            }
            throw exception;
        }
    }

    @Override
    @Transactional
    public void saveRawQuote(UUID quoteId, String payload) {
        if (quoteId == null || payload == null) {
            return;
        }
        rawQuoteRepository.save(RawQuoteEntity.of(quoteId, payload));
    }

    @Override
    @Transactional
    public List<Quote> saveQuotes(List<Quote> quotes) {
        if (quotes == null || quotes.isEmpty()) {
            return List.of();
        }

        var authorById = new LinkedHashMap<UUID, AuthorEntity>();
        var authorByName = new LinkedHashMap<String, AuthorEntity>();
        var quoteEntities = new ArrayList<QuoteEntity>(quotes.size());
        for (Quote quote : quotes) {
            var tagEntities = loadTagEntities(quote.getTags());
            var authorEntity = resolveAuthorEntity(quote.getAuthor(), authorById, authorByName);
            var quoteEntity = quoteMapper.toEntity(quote, authorEntity, tagEntities);
            quoteEntities.add(quoteEntity);
        }

        try {
            var savedQuoteEntities = quoteRepository.saveAll(quoteEntities);
            return savedQuoteEntities.stream()
                    .map(quoteMapper::toDomain)
                    .collect(Collectors.toList());
        } catch (DataIntegrityViolationException exception) {
            if (isDuplicateQuoteViolation(exception)) {
                throw new DuplicatedQuoteException(findDuplicateQuoteText(exception, quotes), exception);
            }
            throw exception;
        }
    }

    @Override
    @Transactional
    public void saveRawQuotes(Map<UUID, String> payloadByQuoteId) {
        if (payloadByQuoteId == null || payloadByQuoteId.isEmpty()) {
            return;
        }

        var entities = payloadByQuoteId.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .map(entry -> RawQuoteEntity.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        if (!entities.isEmpty()) {
            rawQuoteRepository.saveAll(entities);
        }
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
    public List<Quote> findAllQuotes() {
        return quoteRepository.findAll(Sort.by(
                        Sort.Order.asc("author.name"),
                        Sort.Order.asc("quote"),
                        Sort.Order.asc("id")
                )).stream()
                .map(quoteMapper::toDomain)
                .collect(Collectors.toList());
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
    public long countTags() {
        return tagRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quote> findQuotesByTagName(String tagName) {
        return quoteRepository.findAllByTags_Name(tagName).stream()
                .map(quoteMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quote> findTopQuotesByLikes(int limit) {
        if (limit <= 0) {
            return List.of();
        }

        var pageable = PageRequest.of(0, limit, Sort.by(
                Sort.Order.desc("likes"),
                Sort.Order.asc("id")
        ));

        return quoteRepository.findAllByOrderByLikesDescIdAsc(pageable).getContent().stream()
                .map(quoteMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> findAuthorById(UUID id) {
        return authorRepository.findById(id).map(authorMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> findAllAuthors() {
        LinkedHashMap<UUID, AuthorEntity> distinctAuthors = authorRepository.findAll().stream()
                .collect(Collectors.toMap(
                        AuthorEntity::getId,
                        authorEntity -> authorEntity,
                        (existing, duplicate) -> existing,
                        LinkedHashMap::new));

        return distinctAuthors.values().stream()
                .map(authorMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countAuthors() {
        return authorRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorQuotesSummary> findTopAuthorsWithQuoteCount(int limit) {
        if (limit <= 0) {
            return List.of();
        }

        return quoteRepository.findAuthorQuoteCounts(PageRequest.of(0, limit)).stream()
                .map(this::toAuthorQuotesSummary)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteQuoteById(UUID id) {
        quoteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public long incrementQuoteLike(UUID quoteId) {
        return quoteRepository.findById(quoteId)
                .map(entity -> {
                    entity.setLikes(entity.getLikes() + 1);
                    return quoteRepository.save(entity).getLikes();
                })
                .orElseThrow(() -> new IllegalArgumentException("Quote not found: " + quoteId));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, Tag> findAllByIds(Collection<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        return tagRepository.findAllById(ids).stream()
                .map(tagMapper::toDomain)
                .filter(Objects::nonNull)
                .filter(tag -> tag.getId() != null)
                .collect(Collectors.toMap(
                        Tag::getId,
                        tag -> tag,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Tag> findAllByNames(Collection<String> names) {
        if (names == null || names.isEmpty()) {
            return Map.of();
        }

        var normalizedNames = names.stream()
                .map(this::normalizeName)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (normalizedNames.isEmpty()) {
            return Map.of();
        }

        return tagRepository.findAllByNameIn(normalizedNames).stream()
                .map(tagMapper::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        tag -> normalizeName(tag.getName()),
                        tag -> tag,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    @Override
    @Transactional
    public Tag saveTag(String name) {
        var normalizedName = normalizeName(name);
        if (normalizedName == null) {
            throw new IllegalArgumentException("Tag name must not be blank");
        }
        TagEntity entity = tagRepository.save(TagEntity.newInstance(normalizedName));
        return tagMapper.toDomain(entity);
    }

    private Set<TagEntity> loadTagEntities(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return Set.of();
        }

        var ids = tags.stream()
                .map(Tag::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (ids.isEmpty()) {
            return Set.of();
        }

        return tagRepository.findAllById(ids).stream()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private AuthorEntity resolveAuthorEntity(
            Author author,
            Map<UUID, AuthorEntity> authorsById,
            Map<String, AuthorEntity> authorsByName) {
        if (author == null) {
            throw new IllegalArgumentException("Author must not be null");
        }

        var authorId = author.getId();
        var normalizedName = normalizeName(author.getName());
        if (authorId != null) {
            var cached = authorsById.get(authorId);
            if (cached != null) {
                updateAuthorWikipediaUrl(cached, author.getWikipediaUrl());
                return cached;
            }

            var entity = authorRepository.findById(authorId)
                    .map(existing -> {
                        updateAuthorWikipediaUrl(existing, author.getWikipediaUrl());
                        return existing;
                    })
                    .orElseGet(() -> {
                        if (normalizedName != null) {
                            return resolveAuthorEntityByName(author, normalizedName);
                        }
                        return authorMapper.toEntity(author);
                    });

            if (entity.getId() != null) {
                authorsById.putIfAbsent(entity.getId(), entity);
            }
            if (entity.getId() == null || entity.getId().equals(authorId)) {
                authorsById.put(authorId, entity);
            }
            var nameKey = entity.getName() != null ? normalizeName(entity.getName()) : normalizedName;
            if (nameKey != null) {
                authorsByName.putIfAbsent(nameKey, entity);
            }
            return entity;
        }

        if (normalizedName == null) {
            throw new IllegalArgumentException("Author name must not be blank");
        }

        var cached = authorsByName.get(normalizedName);
        if (cached != null) {
            updateAuthorWikipediaUrl(cached, author.getWikipediaUrl());
            return cached;
        }

        var entity = resolveAuthorEntityByName(author, normalizedName);

        authorsByName.put(normalizedName, entity);
        if (entity.getId() != null) {
            authorsById.putIfAbsent(entity.getId(), entity);
        }
        return entity;
    }

    private AuthorEntity resolveAuthorEntityByName(Author author, String normalizedName) {
        if (normalizedName == null) {
            throw new IllegalArgumentException("Author name must not be blank");
        }

        return authorRepository.findByNameIgnoreCase(normalizedName)
                .map(existing -> {
                    updateAuthorWikipediaUrl(existing, author.getWikipediaUrl());
                    return existing;
                })
                .orElseGet(() -> {
                    var newEntity = authorMapper.toEntity(author);
                    if (newEntity.getName() != null) {
                        newEntity.setName(normalizedName);
                    }
                    return newEntity;
                });
    }

    private String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        var trimmed = name.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void updateAuthorWikipediaUrl(AuthorEntity entity,
                                          String wikipediaUrl) {
        if (wikipediaUrl != null && !wikipediaUrl.isBlank()) {
            entity.setWikipediaUrl(wikipediaUrl);
        }
    }

    private AuthorQuotesSummary toAuthorQuotesSummary(AuthorQuoteCountProjection projection) {
        if (projection == null) {
            return null;
        }
        var author = new Author(
                projection.getAuthorId(),
                projection.getAuthorName(),
                projection.getAuthorWikipediaUrl()
        );
        return new AuthorQuotesSummary(author, projection.getQuotesCount());
    }

    private boolean isDuplicateQuoteViolation(Throwable throwable) {
        Throwable cause = throwable;
        while (cause != null) {
            if (cause instanceof ConstraintViolationException constraintViolationException) {
                var constraintName = constraintViolationException.getConstraintName();
                if (constraintName != null && constraintName.equalsIgnoreCase("quote_text_unique_idx")) {
                    return true;
                }
                var sqlState = constraintViolationException.getSQLState();
                if ("23505".equals(sqlState)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    private String findDuplicateQuoteText(DataIntegrityViolationException exception, List<Quote> quotes) {
        var extracted = extractDuplicateQuoteText(exception);
        if (extracted != null && !extracted.isBlank()) {
            return extracted;
        }
        if (quotes != null) {
            for (Quote quote : quotes) {
                if (quote == null) {
                    continue;
                }
                var quoteText = quote.getQuote();
                if (quoteText == null || quoteText.isBlank()) {
                    continue;
                }
                if (quoteRepository.existsByQuote(quoteText)) {
                    return quoteText;
                }
            }
            return findDuplicateQuoteInRequest(quotes);
        }
        return null;
    }

    private String extractDuplicateQuoteText(Throwable throwable) {
        Throwable cause = throwable;
        while (cause != null) {
            if (cause instanceof ConstraintViolationException constraintViolationException) {
                var extracted = extractDuplicateQuoteText(constraintViolationException.getSQLException());
                if (extracted != null) {
                    return extracted;
                }
                extracted = extractDuplicateQuoteText(constraintViolationException.getMessage());
                if (extracted != null) {
                    return extracted;
                }
            }
            cause = cause.getCause();
        }
        return null;
    }

    private String extractDuplicateQuoteText(java.sql.SQLException sqlException) {
        if (sqlException == null) {
            return null;
        }
        var extracted = extractDuplicateQuoteText(sqlException.getMessage());
        if (extracted != null) {
            return extracted;
        }
        return extractDuplicateQuoteText(sqlException.getLocalizedMessage());
    }

    private String extractDuplicateQuoteText(String message) {
        if (message == null || message.isBlank()) {
            return null;
        }
        Matcher matcher = DUPLICATE_QUOTE_DETAIL_PATTERN.matcher(message);
        if (matcher.find()) {
            return sanitizeQuoteText(matcher.group(1));
        }
        return null;
    }

    private String sanitizeQuoteText(String value) {
        if (value == null) {
            return null;
        }
        var trimmed = value.trim();
        if (trimmed.length() >= 2 && ((trimmed.startsWith("\"") && trimmed.endsWith("\""))
                || (trimmed.startsWith("'") && trimmed.endsWith("'")))) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    private String findDuplicateQuoteInRequest(List<Quote> quotes) {
        var seen = new LinkedHashSet<String>();
        for (Quote quote : quotes) {
            if (quote == null) {
                continue;
            }
            var quoteText = quote.getQuote();
            if (quoteText == null || quoteText.isBlank()) {
                continue;
            }
            if (!seen.add(quoteText)) {
                return quoteText;
            }
        }
        return null;
    }

    private static final Pattern DUPLICATE_QUOTE_DETAIL_PATTERN =
            Pattern.compile("Key \\((?i:text)\\)=\\((.*?)\\)");
}
