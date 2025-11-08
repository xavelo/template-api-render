package com.xavelo.filocitas.adapter.in.http.admin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xavelo.filocitas.application.exception.DuplicatedQuoteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class AdminExceptionHandlerTest {

    private ObjectMapper objectMapper;
    private AdminExceptionHandler handler;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        handler = new AdminExceptionHandler(objectMapper);
    }

    @Test
    void returnsConflictResponseWithParsedPayloadWhenJsonIsValid() {
        var duplicatedQuote = new DuplicatedQuoteException("Duplicated quote", new RuntimeException("conflict"))
                .withPayload("{\"quote\":\"Duplicated quote\"}");

        ResponseEntity<JsonNode> response = handler.handleDuplicatedQuote(duplicatedQuote);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().path("error").asText()).isEqualTo("Quote already exists");
        assertThat(response.getBody().path("quote").asText()).isEqualTo("Duplicated quote");
        assertThat(response.getBody().path("payload").path("quote").asText()).isEqualTo("Duplicated quote");
    }

    @Test
    void returnsConflictResponseWithRawPayloadWhenJsonIsInvalid() {
        var duplicatedQuote = new DuplicatedQuoteException("Duplicated quote", new RuntimeException("conflict"))
                .withPayload("not-json");

        ResponseEntity<JsonNode> response = handler.handleDuplicatedQuote(duplicatedQuote);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().path("error").asText()).isEqualTo("Quote already exists");
        assertThat(response.getBody().path("payload_raw").asText()).isEqualTo("not-json");
    }

    @Test
    void returnsConflictResponseWithoutPayloadWhenPayloadIsMissing() {
        var duplicatedQuote = new DuplicatedQuoteException("Duplicated quote", new RuntimeException("conflict"));

        ResponseEntity<JsonNode> response = handler.handleDuplicatedQuote(duplicatedQuote);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().path("error").asText()).isEqualTo("Quote already exists");
        assertThat(response.getBody().has("payload")).isFalse();
        assertThat(response.getBody().has("payload_raw")).isFalse();
    }
}
