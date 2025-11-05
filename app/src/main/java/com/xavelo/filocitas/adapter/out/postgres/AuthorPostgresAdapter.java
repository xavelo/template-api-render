package com.xavelo.filocitas.adapter.out.postgres;

import com.xavelo.filocitas.adapter.out.postgres.mapper.AuthorMapper;
import com.xavelo.filocitas.adapter.out.postgres.repository.AuthorRepository;
import com.xavelo.filocitas.application.domain.author.Author;
import com.xavelo.filocitas.port.out.LoadAuthorPort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class AuthorPostgresAdapter implements LoadAuthorPort {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorPostgresAdapter(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Author> findAuthorById(UUID id) {
        return authorRepository.findById(id).map(authorMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> findAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countAuthors() {
        return authorRepository.count();
    }
}
