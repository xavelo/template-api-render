package com.xavelo.filocitas.adapter.out.postgres.repository;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.QuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuoteRepository extends JpaRepository<QuoteEntity, UUID> {

    @Query(value = "SELECT * FROM quote ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<QuoteEntity> findRandomQuote();

    List<QuoteEntity> findAllByAuthorId(UUID authorId);
}
