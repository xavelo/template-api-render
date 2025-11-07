package com.xavelo.filocitas.port.in;

import com.xavelo.filocitas.application.domain.AuthorQuotesSummary;

import java.util.List;

public interface GetAuthorsQuotesCountUseCase {

    List<AuthorQuotesSummary> getTopAuthorsQuotesCount(int limit);
}
