package com.xavelo.filocitas.port.out;

import java.util.UUID;

public interface IncrementQuoteLikePort {

    long incrementQuoteLike(UUID quoteId);
}
