package com.xavelo.filocitas.adapter.in.http.publicapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xavelo.filocitas.application.domain.quote.Quote;

import java.util.List;
import java.util.UUID;

public record QuoteResponse(
        @JsonProperty("id") UUID id,
        @JsonProperty("author") AuthorResponse author,
        @JsonProperty("work") String work,
        @JsonProperty("year") Integer year,
        @JsonProperty("translator") String translator,
        @JsonProperty("language") String language,
        @JsonProperty("quote") String text,
        @JsonProperty("reference_system") String referenceSystem,
        @JsonProperty("work_part") String workPart,
        @JsonProperty("locator") String locator,
        @JsonProperty("theme_tags") List<String> themeTags,
        @JsonProperty("century") String century,
        @JsonProperty("source_url") String sourceUrl,
        @JsonProperty("source_institution") String sourceInstitution,
        @JsonProperty("license") String license
) {

    public static QuoteResponse fromDomain(Quote quote) {
        AuthorResponse authorResponse = AuthorResponse.fromDomain(quote.getAuthor());

        return new QuoteResponse(
                quote.getId(),
                authorResponse,
                quote.getWork(),
                quote.getYear(),
                quote.getTranslator(),
                quote.getLanguage(),
                quote.getText(),
                quote.getReferenceSystem(),
                quote.getWorkPart(),
                quote.getLocator(),
                quote.getThemeTags(),
                quote.getCentury(),
                quote.getSourceUrl(),
                quote.getSourceInstitution(),
                quote.getLicense()
        );
    }
}
