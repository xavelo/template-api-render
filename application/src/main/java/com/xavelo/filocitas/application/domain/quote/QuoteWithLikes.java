package com.xavelo.filocitas.application.domain.quote;

import java.util.Objects;

public class QuoteWithLikes {

    private final Quote quote;
    private final long likes;

    public QuoteWithLikes(Quote quote, long likes) {
        this.quote = Objects.requireNonNull(quote, "quote must not be null");
        this.likes = likes;
    }

    public Quote getQuote() {
        return quote;
    }

    public long getLikes() {
        return likes;
    }

    public QuoteWithLikes withQuote(Quote quote) {
        return new QuoteWithLikes(quote, likes);
    }

    public QuoteWithLikes withLikes(long likes) {
        return new QuoteWithLikes(quote, likes);
    }
}
