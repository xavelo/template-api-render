package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Authorization;
import java.time.Instant;
import java.util.UUID;

public interface CreateAuthorizationUseCase {
    Authorization createAuthorization(String title, String text, String status, UUID createdBy, String sentBy,
                                      String approvedBy, Instant expiresAt);
}
