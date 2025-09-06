package com.xavelo.template.render.api.application.port.out;

import com.xavelo.template.render.api.domain.Guardian;

import java.util.Optional;
import java.util.UUID;

public interface GetGuardianPort {
    Optional<Guardian> getGuardian(UUID id);
}
