package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Authorization;
import java.time.Instant;

public interface CreateAuthorizationUseCase {
    Authorization createAuthorization(String title, String text, String status, String createdBy, String sentBy,
                                      String approvedBy, Instant expiresAt);
}
