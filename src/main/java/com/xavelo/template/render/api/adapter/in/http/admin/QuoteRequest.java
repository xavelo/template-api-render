package com.xavelo.template.render.api.adapter.in.http.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xavelo.template.render.domain.author.Author;
import com.xavelo.template.render.domain.quote.Quote;

import java.util.List;

public record QuoteRequest(
        @JsonProperty("author") String authorName,
        @JsonProperty("author_wikipedia") String authorWikipedia,
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

    public Quote toDomainQuote() {
        Author author = new Author(authorName, authorWikipedia);
        return new Quote(
                author,
                work,
                year,
                translator,
                language,
                text,
                referenceSystem,
                workPart,
                locator,
                themeTags,
                century,
                sourceUrl,
                sourceInstitution,
                license
        );
    }
}
