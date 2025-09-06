package com.xavelo.template.render.api.domain;

import java.time.Instant;
import java.util.UUID;

public record Notification(
        UUID id,
        UUID authorizationId,
        UUID studentId,
        UUID guardianId,
        String status,
        Instant sentAt,
        Instant respondedAt,
        String respondedBy
) {}
