package com.xavelo.filocitas.adapter.in.http.mapper;

import com.xavelo.filocitas.api.model.CountResponse;
import com.xavelo.filocitas.api.model.PingResponse;
import com.xavelo.filocitas.api.model.QuoteExportResponse;
import com.xavelo.filocitas.api.model.QuoteLikesResponse;
import com.xavelo.filocitas.api.model.QuoteRequest;
import com.xavelo.filocitas.application.domain.Author;
import com.xavelo.filocitas.application.domain.AuthorQuotesSummary;
import com.xavelo.filocitas.application.domain.Quote;
import com.xavelo.filocitas.application.domain.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApiMapper {

    com.xavelo.filocitas.api.model.Author toApiAuthor(Author author);

    List<com.xavelo.filocitas.api.model.Author> toApiAuthors(List<Author> authors);

    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "likes", source = "likes")
    com.xavelo.filocitas.api.model.Quote toApiQuote(Quote quote);

    List<com.xavelo.filocitas.api.model.Quote> toApiQuotes(List<Quote> quotes);

    default QuoteRequest toApiQuoteRequest(Quote quote) {
        if (quote == null) {
            return null;
        }

        QuoteRequest request = new QuoteRequest();
        if (quote.getAuthor() != null) {
            request.setAuthor(quote.getAuthor().getName());
            String wikipediaUrl = quote.getAuthor().getWikipediaUrl();
            if (wikipediaUrl != null && !wikipediaUrl.isBlank()) {
                try {
                    request.setAuthorWikipedia(URI.create(wikipediaUrl));
                } catch (IllegalArgumentException ignored) {
                    request.setAuthorWikipedia(null);
                }
            }
        } else {
            request.setAuthor("");
        }

        request.setQuote(quote.getQuote());
        request.setWork(normalizeOptionalValue(quote.getWork()));
        request.setYear(quote.getYear());
        request.setCentury(normalizeOptionalValue(quote.getCentury()));

        List<String> tags = quote.getTags() == null
                ? List.of()
                : quote.getTags().stream()
                .map(this::map)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toList());
        request.setTags(tags.isEmpty() ? List.of() : tags);

        return request;
    }

    default QuoteExportResponse toApiQuoteExportResponse(List<Quote> quotes) {
        QuoteExportResponse response = new QuoteExportResponse();
        if (quotes == null || quotes.isEmpty()) {
            response.setQuotes(List.of());
            return response;
        }
        List<QuoteRequest> exportableQuotes = quotes.stream()
                .map(this::toApiQuoteRequest)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        response.setQuotes(exportableQuotes);
        return response;
    }

    com.xavelo.filocitas.api.model.AuthorQuotesCount toApiAuthorQuotesCount(AuthorQuotesSummary summary);

    List<com.xavelo.filocitas.api.model.AuthorQuotesCount> toApiAuthorQuotesCounts(List<AuthorQuotesSummary> summaries);

    default CountResponse toCountResponse(long count) {
        CountResponse response = new CountResponse();
        response.setCount(count);
        return response;
    }

    default PingResponse toPingResponse(String message) {
        PingResponse response = new PingResponse();
        response.setMessage(message);
        return response;
    }

    default QuoteLikesResponse toQuoteLikesResponse(long likes) {
        QuoteLikesResponse response = new QuoteLikesResponse();
        response.setLikes(likes);
        return response;
    }

    default Quote toDomainQuote(QuoteRequest request) {
        if (request == null) {
            return null;
        }
        String wikipediaUrl = request.getAuthorWikipedia() != null
                ? request.getAuthorWikipedia().toString()
                : null;
        Author author = new Author(request.getAuthor(), wikipediaUrl);
        List<Tag> tags = request.getTags() == null
                ? List.of()
                : request.getTags().stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(tagName -> !tagName.isEmpty())
                .map(Tag::new)
                .toList();
        return new Quote(
                author,
                request.getWork(),
                request.getYear(),
                request.getQuote(),
                tags,
                request.getCentury()
        );
    }

    default String map(Tag tag) {
        if (tag == null) {
            return null;
        }
        return tag.getName();
    }

    private String normalizeOptionalValue(String value) {
        if (value == null) {
            return null;
        }
        var trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    default List<Quote> toDomainQuotes(List<QuoteRequest> requests) {
        if (requests == null) {
            return List.of();
        }
        return requests.stream()
                .map(this::toDomainQuote)
                .filter(Objects::nonNull)
                .toList();
    }

}
