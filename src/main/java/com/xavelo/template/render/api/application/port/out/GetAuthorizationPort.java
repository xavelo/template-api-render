package com.xavelo.template.render.api.application.port.out;

import com.xavelo.template.render.api.domain.Authorization;

import java.util.Optional;
import java.util.UUID;

public interface GetAuthorizationPort {
    Optional<Authorization> getAuthorization(UUID authorizationId);
}
