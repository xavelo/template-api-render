package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Guardian;

public interface CreateGuardianUseCase {
    Guardian createGuardian(String name, String email);
}
