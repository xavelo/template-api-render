package com.xavelo.filocitas.port.out;

import java.util.Map;
import java.util.UUID;

public interface SaveRawQuotePort {

    void saveRawQuote(UUID quoteId, String payload);

    void saveRawQuotes(Map<UUID, String> payloadByQuoteId);
}
