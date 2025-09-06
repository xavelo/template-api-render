package com.xavelo.template.render.api.domain;

import java.time.Instant;
import java.util.UUID;

public record Authorization(
        UUID id,
        String title,
        String text,
        String status,
        Instant createdAt,
        String createdBy,
        Instant sentAt,
        String sentBy,
        Instant approvedAt,
        String approvedBy
) {}
