package com.xavelo.filocitas.adapter.out.postgres.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "raw_quotes")
public class RawQuoteEntity {

    @Id
    @Column(name = "quote_id", nullable = false, updatable = false)
    private UUID quoteId;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    protected RawQuoteEntity() {
        // JPA
    }

    private RawQuoteEntity(UUID quoteId, String payload) {
        this.quoteId = quoteId;
        this.payload = payload;
    }

    public static RawQuoteEntity of(UUID quoteId, String payload) {
        return new RawQuoteEntity(quoteId, payload);
    }

    public UUID getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(UUID quoteId) {
        this.quoteId = quoteId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
