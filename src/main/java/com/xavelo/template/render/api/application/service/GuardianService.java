package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.CreateGuardianUseCase;
import com.xavelo.template.render.api.application.port.out.CreateGuardianPort;
import com.xavelo.template.render.api.domain.Guardian;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GuardianService implements CreateGuardianUseCase {

    private final CreateGuardianPort createGuardianPort;

    public GuardianService(CreateGuardianPort createGuardianPort) {
        this.createGuardianPort = createGuardianPort;
    }

    @Override
    public Guardian createGuardian(String name) {
        Guardian guardian = new Guardian(UUID.randomUUID(), name);
        return createGuardianPort.createGuardian(guardian);
    }
}
