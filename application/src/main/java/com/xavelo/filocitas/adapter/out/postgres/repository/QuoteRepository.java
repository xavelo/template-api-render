package com.xavelo.filocitas.adapter.out.postgres.repository;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.QuoteEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.projection.AuthorQuoteCountProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuoteRepository extends JpaRepository<QuoteEntity, UUID> {

    @Query(value = "SELECT * FROM quotes ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<QuoteEntity> findRandomQuote();

    List<QuoteEntity> findAllByAuthorId(UUID authorId);

    List<QuoteEntity> findAllByTags_Name(String tagName);

    Page<QuoteEntity> findAllByOrderByLikesDescIdAsc(Pageable pageable);

    boolean existsByQuote(String quote);

    long countDistinctByTags_Id(UUID tagId);

    @Query("""
            SELECT q.author.id AS authorId,
                   q.author.name AS authorName,
                   q.author.wikipediaUrl AS authorWikipediaUrl,
                   COUNT(q.id) AS quotesCount
            FROM QuoteEntity q
            GROUP BY q.author.id, q.author.name, q.author.wikipediaUrl
            ORDER BY COUNT(q.id) DESC, q.author.name ASC
            """)
    List<AuthorQuoteCountProjection> findAuthorQuoteCounts(Pageable pageable);
}
