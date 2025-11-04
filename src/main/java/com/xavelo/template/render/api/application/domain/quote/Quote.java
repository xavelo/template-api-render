package com.xavelo.template.render.api.application.domain.quote;

import com.xavelo.template.render.api.application.domain.author.Author;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Quote {

    private final Author author;
    private final String work;
    private final Integer year;
    private final String translator;
    private final String language;
    private final String text;
    private final String referenceSystem;
    private final String workPart;
    private final String locator;
    private final List<String> themeTags;
    private final String century;
    private final String sourceUrl;
    private final String sourceInstitution;
    private final String license;

    public Quote(
            Author author,
            String work,
            Integer year,
            String translator,
            String language,
            String text,
            String referenceSystem,
            String workPart,
            String locator,
            List<String> themeTags,
            String century,
            String sourceUrl,
            String sourceInstitution,
            String license
    ) {
        this.author = Objects.requireNonNull(author, "author must not be null");
        this.work = Objects.requireNonNullElse(work, "");
        this.year = year;
        this.translator = Objects.requireNonNullElse(translator, "");
        this.language = Objects.requireNonNullElse(language, "");
        this.text = Objects.requireNonNullElse(text, "");
        this.referenceSystem = Objects.requireNonNullElse(referenceSystem, "");
        this.workPart = Objects.requireNonNullElse(workPart, "");
        this.locator = Objects.requireNonNullElse(locator, "");
        this.themeTags = Collections.unmodifiableList(themeTags == null ? List.of() : List.copyOf(themeTags));
        this.century = Objects.requireNonNullElse(century, "");
        this.sourceUrl = Objects.requireNonNullElse(sourceUrl, "");
        this.sourceInstitution = Objects.requireNonNullElse(sourceInstitution, "");
        this.license = Objects.requireNonNullElse(license, "");
    }

    public Author getAuthor() {
        return author;
    }

    public String getWork() {
        return work;
    }

    public Integer getYear() {
        return year;
    }

    public String getTranslator() {
        return translator;
    }

    public String getLanguage() {
        return language;
    }

    public String getText() {
        return text;
    }

    public String getReferenceSystem() {
        return referenceSystem;
    }

    public String getWorkPart() {
        return workPart;
    }

    public String getLocator() {
        return locator;
    }

    public List<String> getThemeTags() {
        return themeTags;
    }

    public String getCentury() {
        return century;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getSourceInstitution() {
        return sourceInstitution;
    }

    public String getLicense() {
        return license;
    }
}
