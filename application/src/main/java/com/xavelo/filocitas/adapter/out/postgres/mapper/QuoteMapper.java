package com.xavelo.filocitas.adapter.out.postgres.mapper;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.AuthorEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.QuoteEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.TagEntity;
import com.xavelo.filocitas.application.domain.author.Author;
import com.xavelo.filocitas.application.domain.quote.Quote;
import com.xavelo.filocitas.application.domain.tag.Tag;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class QuoteMapper {

    private final AuthorMapper authorMapper;
    private final TagMapper tagMapper;

    public QuoteMapper(AuthorMapper authorMapper, TagMapper tagMapper) {
        this.authorMapper = authorMapper;
        this.tagMapper = tagMapper;
    }

    public QuoteEntity toEntity(Quote quote, Set<TagEntity> tags) {
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
        quoteEntity.setTags(tags == null ? new LinkedHashSet<>() : new LinkedHashSet<>(tags));
        quoteEntity.setCentury(quote.getCentury());
        quoteEntity.setSourceUrl(quote.getSourceUrl());
        quoteEntity.setSourceInstitution(quote.getSourceInstitution());
        quoteEntity.setLicense(quote.getLicense());
        return quoteEntity;
    }

    public Quote toDomain(QuoteEntity quoteEntity) {
        var authorEntity = quoteEntity.getAuthor();
        Author author = authorMapper.toDomain(authorEntity);

        List<Tag> tags = tagMapper.toDomainList(quoteEntity.getTags());

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
