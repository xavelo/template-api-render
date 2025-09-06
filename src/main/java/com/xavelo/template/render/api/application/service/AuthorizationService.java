package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.AssignStudentsToAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.CreateAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.out.CreateAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.AssignStudentsToAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.GetUserPort;
import com.xavelo.template.render.api.domain.Authorization;
import com.xavelo.template.render.api.application.exception.UserNotExistException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorizationService implements CreateAuthorizationUseCase, AssignStudentsToAuthorizationUseCase {

    private final CreateAuthorizationPort createAuthorizationPort;
    private final AssignStudentsToAuthorizationPort assignStudentsToAuthorizationPort;
    private final GetUserPort getUserPort;

    public AuthorizationService(CreateAuthorizationPort createAuthorizationPort,
                                AssignStudentsToAuthorizationPort assignStudentsToAuthorizationPort,
                                GetUserPort getUserPort) {
        this.createAuthorizationPort = createAuthorizationPort;
        this.assignStudentsToAuthorizationPort = assignStudentsToAuthorizationPort;
        this.getUserPort = getUserPort;
    }

    @Override
    public Authorization createAuthorization(String title, String text, String status, String createdBy, String sentBy, String approvedBy) {
        UUID createdById = UUID.fromString(createdBy);
        getUserPort.getUser(createdById)
                .orElseThrow(UserNotExistException::new);

        Authorization authorization = new Authorization(UUID.randomUUID(), title, text, status, null, createdBy, null, sentBy, null, approvedBy);
        return createAuthorizationPort.createAuthorization(authorization);
    }

    @Override
    public void assignStudents(UUID authorizationId, java.util.List<UUID> studentIds) {
        assignStudentsToAuthorizationPort.assignStudentsToAuthorization(authorizationId, studentIds);
    }
}
