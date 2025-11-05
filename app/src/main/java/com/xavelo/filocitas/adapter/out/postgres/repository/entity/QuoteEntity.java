package com.xavelo.filocitas.adapter.out.postgres.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "quote")
public class QuoteEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", nullable = false)
    private AuthorEntity author;

    @Column(name = "work")
    private String work;

    @Column(name = "year")
    private Integer year;

    @Column(name = "translator")
    private String translator;

    @Column(name = "language")
    private String language;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "reference_system")
    private String referenceSystem;

    @Column(name = "work_part")
    private String workPart;

    @Column(name = "locator")
    private String locator;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "theme_tags", columnDefinition = "text[]")
    private List<String> themeTags = new ArrayList<>();

    @Column(name = "century")
    private String century;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "source_institution")
    private String sourceInstitution;

    @Column(name = "license")
    private String license;

    protected QuoteEntity() {
        // JPA
    }

    public static QuoteEntity newInstance() {
        return new QuoteEntity();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReferenceSystem() {
        return referenceSystem;
    }

    public void setReferenceSystem(String referenceSystem) {
        this.referenceSystem = referenceSystem;
    }

    public String getWorkPart() {
        return workPart;
    }

    public void setWorkPart(String workPart) {
        this.workPart = workPart;
    }

    public String getLocator() {
        return locator;
    }

    public void setLocator(String locator) {
        this.locator = locator;
    }

    public List<String> getThemeTags() {
        return themeTags;
    }

    public void setThemeTags(List<String> themeTags) {
        this.themeTags = themeTags;
    }

    public String getCentury() {
        return century;
    }

    public void setCentury(String century) {
        this.century = century;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceInstitution() {
        return sourceInstitution;
    }

    public void setSourceInstitution(String sourceInstitution) {
        this.sourceInstitution = sourceInstitution;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
