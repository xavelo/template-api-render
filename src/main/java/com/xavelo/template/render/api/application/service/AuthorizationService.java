package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.AssignStudentsToAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.CreateAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.out.CreateAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.AssignStudentsToAuthorizationPort;
import com.xavelo.template.render.api.domain.Authorization;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorizationService implements CreateAuthorizationUseCase, AssignStudentsToAuthorizationUseCase {

    private final CreateAuthorizationPort createAuthorizationPort;
    private final AssignStudentsToAuthorizationPort assignStudentsToAuthorizationPort;

    public AuthorizationService(CreateAuthorizationPort createAuthorizationPort,
                                AssignStudentsToAuthorizationPort assignStudentsToAuthorizationPort) {
        this.createAuthorizationPort = createAuthorizationPort;
        this.assignStudentsToAuthorizationPort = assignStudentsToAuthorizationPort;
    }

    @Override
    public Authorization createAuthorization(String title, String text, String status, String createdBy, String sentBy, String approvedBy) {
        Authorization authorization = new Authorization(UUID.randomUUID(), title, text, status, null, createdBy, null, sentBy, null, approvedBy);
        return createAuthorizationPort.createAuthorization(authorization);
    }

    @Override
    public void assignStudents(UUID authorizationId, java.util.List<UUID> studentIds) {
        assignStudentsToAuthorizationPort.assignStudentsToAuthorization(authorizationId, studentIds);
    }
}
