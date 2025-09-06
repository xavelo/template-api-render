package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Guardian;

import java.util.Optional;
import java.util.UUID;

public interface GetGuardianUseCase {
    Optional<Guardian> getGuardian(UUID id);
}
