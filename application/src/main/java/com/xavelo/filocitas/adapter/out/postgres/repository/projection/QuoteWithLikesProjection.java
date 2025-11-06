package com.xavelo.filocitas.adapter.out.postgres.repository.projection;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.QuoteEntity;

public interface QuoteWithLikesProjection {

    QuoteEntity getQuote();

    Long getLikes();
}
