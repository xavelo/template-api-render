package com.xavelo.filocitas.adapter.out.postgres.repository.projection;

import java.util.UUID;

public interface AuthorQuoteCountProjection {

    UUID getAuthorId();

    String getAuthorName();

    String getAuthorWikipediaUrl();

    long getQuotesCount();
}
