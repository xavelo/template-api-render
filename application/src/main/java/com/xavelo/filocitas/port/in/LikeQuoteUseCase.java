package com.xavelo.filocitas.port.in;

import java.util.Optional;
import java.util.UUID;

public interface LikeQuoteUseCase {

    Optional<Long> likeQuote(UUID quoteId);
}
