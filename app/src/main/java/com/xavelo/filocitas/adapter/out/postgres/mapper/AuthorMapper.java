package com.xavelo.filocitas.adapter.out.postgres.mapper;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.AuthorEntity;
import com.xavelo.filocitas.application.domain.author.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public AuthorEntity toEntity(Author author) {
        if (author == null) {
            return null;
        }
        return new AuthorEntity(
                author.getId(),
                author.getName(),
                author.getWikipediaUrl()
        );
    }

    public Author toDomain(AuthorEntity authorEntity) {
        if (authorEntity == null) {
            return null;
        }
        return new Author(
                authorEntity.getId(),
                authorEntity.getName(),
                authorEntity.getWikipediaUrl()
        );
    }
}
