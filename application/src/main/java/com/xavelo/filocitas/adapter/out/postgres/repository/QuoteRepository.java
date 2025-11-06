package com.xavelo.filocitas.adapter.out.postgres.repository;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.QuoteEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.QuoteLikeEntity;
import com.xavelo.filocitas.adapter.out.postgres.repository.projection.QuoteWithLikesProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuoteRepository extends JpaRepository<QuoteEntity, UUID> {

    @Query(value = "SELECT * FROM quote ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<QuoteEntity> findRandomQuote();

    List<QuoteEntity> findAllByAuthorId(UUID authorId);

    List<QuoteEntity> findAllByTags_Name(String tagName);

    @Query("""
            SELECT q as quote, COALESCE(ql.likes, 0) as likes
            FROM QuoteEntity q
            LEFT JOIN QuoteLikeEntity ql ON q.id = ql.quoteId
            ORDER BY COALESCE(ql.likes, 0) DESC, q.id ASC
            """)
    List<QuoteWithLikesProjection> findTopQuotesByLikes(Pageable pageable);
}
