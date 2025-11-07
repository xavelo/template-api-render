package com.xavelo.filocitas.adapter.out.postgres.mapper;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.AuthorEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.QuoteEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.TagEntity;
import com.xavelo.filocitas.application.domain.Author;
import com.xavelo.filocitas.application.domain.Quote;
import com.xavelo.filocitas.application.domain.Tag;
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

    public QuoteEntity toEntity(Quote quote, AuthorEntity authorEntity, Set<TagEntity> tags) {
        var quoteEntity = QuoteEntity.newInstance();
        quoteEntity.setId(quote.getId());
        quoteEntity.setAuthor(authorEntity);
        quoteEntity.setWork(quote.getWork());
        quoteEntity.setYear(quote.getYear());
        quoteEntity.setQuote(quote.getQuote());
        quoteEntity.setTags(tags == null ? new LinkedHashSet<>() : new LinkedHashSet<>(tags));
        quoteEntity.setCentury(quote.getCentury());
        quoteEntity.setLikes(quote.getLikes());
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
                quoteEntity.getQuote(),
                tags,
                quoteEntity.getCentury(),
                quoteEntity.getLikes()
        );
    }
}
