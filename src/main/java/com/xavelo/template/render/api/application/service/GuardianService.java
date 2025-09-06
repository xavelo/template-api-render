package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.CreateGuardianUseCase;
import com.xavelo.template.render.api.application.port.in.ListGuardiansUseCase;
import com.xavelo.template.render.api.application.port.out.CreateGuardianPort;
import com.xavelo.template.render.api.application.port.out.ListGuardiansPort;
import com.xavelo.template.render.api.domain.Guardian;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class GuardianService implements CreateGuardianUseCase, ListGuardiansUseCase {

    private final CreateGuardianPort createGuardianPort;
    private final ListGuardiansPort listGuardiansPort;

    public GuardianService(CreateGuardianPort createGuardianPort, ListGuardiansPort listGuardiansPort) {
        this.createGuardianPort = createGuardianPort;
        this.listGuardiansPort = listGuardiansPort;
    }

    @Override
    public List<Guardian> listGuardians() {
        return listGuardiansPort.listGuardians();
    }

    @Override
    public Guardian createGuardian(String name, String email) {
        Objects.requireNonNull(email, "email must not be null");
        Guardian guardian = new Guardian(UUID.randomUUID(), name, email);
        return createGuardianPort.createGuardian(guardian);
    }
}
