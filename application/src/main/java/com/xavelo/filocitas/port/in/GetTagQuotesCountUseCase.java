package com.xavelo.filocitas.port.in;

import java.util.UUID;

public interface GetTagQuotesCountUseCase {

    long getTagQuotesCount(UUID tagId);
}
