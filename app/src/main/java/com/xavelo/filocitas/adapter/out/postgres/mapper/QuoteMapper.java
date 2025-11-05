package com.xavelo.filocitas.adapter.out.postgres.mapper;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.AuthorEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.QuoteEntity;
import com.xavelo.filocitas.application.domain.author.Author;
import com.xavelo.filocitas.application.domain.quote.Quote;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuoteMapper {

    private final AuthorMapper authorMapper;

    public QuoteMapper(AuthorMapper authorMapper) {
        this.authorMapper = authorMapper;
    }

    public QuoteEntity toEntity(Quote quote) {
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
        quoteEntity.setThemeTags(new ArrayList<>(quote.getThemeTags()));
        quoteEntity.setCentury(quote.getCentury());
        quoteEntity.setSourceUrl(quote.getSourceUrl());
        quoteEntity.setSourceInstitution(quote.getSourceInstitution());
        quoteEntity.setLicense(quote.getLicense());
        return quoteEntity;
    }

    public Quote toDomain(QuoteEntity quoteEntity) {
        var authorEntity = quoteEntity.getAuthor();
        Author author = authorMapper.toDomain(authorEntity);

        List<String> themeTags = quoteEntity.getThemeTags() == null
                ? List.of()
                : List.copyOf(quoteEntity.getThemeTags());

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
                themeTags,
                quoteEntity.getCentury(),
                quoteEntity.getSourceUrl(),
                quoteEntity.getSourceInstitution(),
                quoteEntity.getLicense()
        );
    }
}
