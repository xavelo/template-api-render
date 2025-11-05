package com.xavelo.filocitas.adapter.in.http.mapper;

import com.xavelo.filocitas.api.model.AuthorInput;
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
import java.util.UUID;

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
        Author author = toDomainAuthor(request.getAuthor());
        return new Quote(
                author,
                null,
                null,
                null,
                null,
                request.getText(),
                null,
                null,
                null,
                List.of(),
                null,
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

    default Author toDomainAuthor(AuthorInput author) {
        if (author == null) {
            return null;
        }
        UUID id = author.getId();
        if (id != null) {
            return new Author(id, author.getName(), "");
        }
        return new Author(author.getName(), "");
    }
}
