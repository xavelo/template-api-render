package com.xavelo.filocitas.port.in;

import java.util.Optional;
import java.util.UUID;

public interface GetQuoteLikesUseCase {

    Optional<Long> getQuoteLikes(UUID quoteId);
}
