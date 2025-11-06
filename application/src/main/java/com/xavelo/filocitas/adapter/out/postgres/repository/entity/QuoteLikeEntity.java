package com.xavelo.filocitas.adapter.out.postgres.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "quote_like")
public class QuoteLikeEntity {

    @Id
    @Column(name = "quote_id", nullable = false)
    private UUID quoteId;

    @Column(name = "likes", nullable = false)
    private long likes;

    protected QuoteLikeEntity() {
        // JPA constructor
    }

    private QuoteLikeEntity(UUID quoteId, long likes) {
        this.quoteId = quoteId;
        this.likes = likes;
    }

    public static QuoteLikeEntity newInstance(UUID quoteId, long likes) {
        return new QuoteLikeEntity(quoteId, likes);
    }

    public UUID getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(UUID quoteId) {
        this.quoteId = quoteId;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }
}
