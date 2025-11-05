package com.xavelo.filocitas.adapter.out.postgres.mapper;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.AuthorEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.QuoteEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.TagEntity;
import com.xavelo.filocitas.application.domain.author.Author;
import com.xavelo.filocitas.application.domain.quote.Quote;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class QuoteMapper {

    private final AuthorMapper authorMapper;

    public QuoteMapper(AuthorMapper authorMapper) {
        this.authorMapper = authorMapper;
    }

    public QuoteEntity toEntity(Quote quote, List<TagEntity> tagEntities) {
        AuthorEntity authorEntity = authorMapper.toEntity(quote.getAuthor());

        var quoteEntity = QuoteEntity.newInstance();
        quoteEntity.setId(quote.getId());
        quoteEntity.setAuthor(authorEntity);
        quoteEntity.setWork(quote.getWork());
        quoteEntity.setYear(quote.getYear());
        quoteEntity.setTranslator(quote.getTranslator());
        quoteEntity.setLanguage(quote.getLanguage());
        quoteEntity.setText(quote.getText());
        quoteEntity.setReferenceSystem(quote.getReferenceSystem());
        quoteEntity.setWorkPart(quote.getWorkPart());
        quoteEntity.setLocator(quote.getLocator());
        quoteEntity.setTags(tagEntities == null ? new LinkedHashSet<>() : new LinkedHashSet<>(tagEntities));
        quoteEntity.setCentury(quote.getCentury());
        quoteEntity.setSourceUrl(quote.getSourceUrl());
        quoteEntity.setSourceInstitution(quote.getSourceInstitution());
        quoteEntity.setLicense(quote.getLicense());
        return quoteEntity;
    }

    public Quote toDomain(QuoteEntity quoteEntity) {
        var authorEntity = quoteEntity.getAuthor();
        Author author = authorMapper.toDomain(authorEntity);

        Set<TagEntity> tagEntities = quoteEntity.getTags();
        List<String> tags = tagEntities == null
                ? List.of()
                : tagEntities.stream()
                .map(TagEntity::getName)
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());

        return new Quote(
                quoteEntity.getId(),
                author,
                quoteEntity.getWork(),
                quoteEntity.getYear(),
                quoteEntity.getTranslator(),
                quoteEntity.getLanguage(),
                quoteEntity.getText(),
                quoteEntity.getReferenceSystem(),
                quoteEntity.getWorkPart(),
                quoteEntity.getLocator(),
                tags,
                quoteEntity.getCentury(),
                quoteEntity.getSourceUrl(),
                quoteEntity.getSourceInstitution(),
                quoteEntity.getLicense()
        );
    }
}
