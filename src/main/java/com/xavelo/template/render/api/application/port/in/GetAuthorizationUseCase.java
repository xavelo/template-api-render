package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Authorization;

import java.util.Optional;
import java.util.UUID;

public interface GetAuthorizationUseCase {
    Optional<Authorization> getAuthorization(UUID authorizationId);
}
