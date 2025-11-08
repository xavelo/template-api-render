package com.xavelo.filocitas.adapter.in.http.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record ApiErrorResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant timestamp,
        String message
) {

    public static ApiErrorResponse fromMessage(String message) {
        return new ApiErrorResponse(Instant.now(), message);
    }
}

