package com.xavelo.filocitas.port.out;

import java.util.UUID;

public interface LikeQuotePort {

    long incrementQuoteLike(UUID quoteId);
}
