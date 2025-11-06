package com.xavelo.filocitas.adapter.in.http.mapper;

import com.xavelo.filocitas.api.model.CountResponse;
import com.xavelo.filocitas.api.model.PingResponse;
import com.xavelo.filocitas.api.model.QuoteRequest;
import com.xavelo.filocitas.application.domain.author.Author;
import com.xavelo.filocitas.application.domain.quote.Quote;
import com.xavelo.filocitas.application.domain.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApiMapper {

    com.xavelo.filocitas.api.model.Author toApiAuthor(Author author);

    List<com.xavelo.filocitas.api.model.Author> toApiAuthors(List<Author> authors);

    @Mapping(target = "tags", source = "tags")
    com.xavelo.filocitas.api.model.Quote toApiQuote(Quote quote);

    List<com.xavelo.filocitas.api.model.Quote> toApiQuotes(List<Quote> quotes);

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

    default Quote toDomainQuote(QuoteRequest request) {
        if (request == null) {
            return null;
        }
        Author author = new Author(request.getAuthor(), request.getAuthorWikipedia());
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
                null,
                null,
                request.getQuote(),
                null,
                null,
                null,
                tags,
                request.getCentury(),
                null,
                null,
                null
        );
    }

    default String map(Tag tag) {
        if (tag == null) {
            return null;
        }
        return tag.getName();
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
