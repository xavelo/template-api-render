package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.AssignStudentsToAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.CreateAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.ListAuthorizationsUseCase;
import com.xavelo.template.render.api.application.port.out.CreateAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.AssignStudentsToAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.GetUserPort;
import com.xavelo.template.render.api.application.port.out.ListAuthorizationsPort;
import com.xavelo.template.render.api.domain.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AuthorizationService implements CreateAuthorizationUseCase, AssignStudentsToAuthorizationUseCase, ListAuthorizationsUseCase {

    private final CreateAuthorizationPort createAuthorizationPort;
    private final AssignStudentsToAuthorizationPort assignStudentsToAuthorizationPort;
    private final ListAuthorizationsPort listAuthorizationsPort;
    private final GetUserPort getUserPort;

    public AuthorizationService(CreateAuthorizationPort createAuthorizationPort,
                                AssignStudentsToAuthorizationPort assignStudentsToAuthorizationPort,
                                ListAuthorizationsPort listAuthorizationsPort,
                                GetUserPort getUserPort) {
        this.createAuthorizationPort = createAuthorizationPort;
        this.assignStudentsToAuthorizationPort = assignStudentsToAuthorizationPort;
        this.listAuthorizationsPort = listAuthorizationsPort;
        this.getUserPort = getUserPort;
    }

    @Override
    public Authorization createAuthorization(String title, String text, String status, String createdBy, String sentBy, String approvedBy) {
        UUID createdByUuid = UUID.fromString(createdBy);
        if (getUserPort.getUser(createdByUuid).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "created_by user does not exist");
        }

        Authorization authorization = new Authorization(UUID.randomUUID(), title, text, status, null, createdBy, null, sentBy, null, approvedBy);
        return createAuthorizationPort.createAuthorization(authorization);
    }

    @Override
    public void assignStudents(UUID authorizationId, java.util.List<UUID> studentIds) {
        assignStudentsToAuthorizationPort.assignStudentsToAuthorization(authorizationId, studentIds);
    }

    @Override
    public List<Authorization> listAuthorizations() {
        return listAuthorizationsPort.listAuthorizations();
    }
}
