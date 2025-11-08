package com.xavelo.filocitas.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuoteExportResponse {

    @JsonProperty("quotes")
    private List<QuoteRequest> quotes = new ArrayList<>();

    public QuoteExportResponse quotes(List<QuoteRequest> quotes) {
        this.quotes = quotes == null ? new ArrayList<>() : new ArrayList<>(quotes);
        return this;
    }

    public List<QuoteRequest> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<QuoteRequest> quotes) {
        this.quotes = quotes == null ? new ArrayList<>() : new ArrayList<>(quotes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QuoteExportResponse that = (QuoteExportResponse) o;
        return Objects.equals(quotes, that.quotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quotes);
    }

    @Override
    public String toString() {
        return "QuoteExportResponse{" +
                "quotes=" + quotes +
                '}';
    }
}
