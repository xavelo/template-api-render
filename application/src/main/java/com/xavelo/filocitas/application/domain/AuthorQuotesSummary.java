package com.xavelo.filocitas.application.domain;

import java.util.Objects;

public class AuthorQuotesSummary {

    private final Author author;
    private final long quotesCount;

    public AuthorQuotesSummary(Author author, long quotesCount) {
        this.author = Objects.requireNonNull(author, "author must not be null");
        if (quotesCount < 0) {
            throw new IllegalArgumentException("quotesCount must not be negative");
        }
        this.quotesCount = quotesCount;
    }

    public Author getAuthor() {
        return author;
    }

    public long getQuotesCount() {
        return quotesCount;
    }
}
