package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.out.AssignStudentsToAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.CreateAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.GetUserPort;
import com.xavelo.template.render.api.application.exception.UserNotExistException;
import com.xavelo.template.render.api.domain.Authorization;
import com.xavelo.template.render.api.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class AuthorizationServiceTest {

    private final CreateAuthorizationPort createAuthorizationPort = Mockito.mock(CreateAuthorizationPort.class);
    private final AssignStudentsToAuthorizationPort assignStudentsToAuthorizationPort = Mockito.mock(AssignStudentsToAuthorizationPort.class);
    private final GetUserPort getUserPort = Mockito.mock(GetUserPort.class);
    private final AuthorizationService authorizationService =
            new AuthorizationService(createAuthorizationPort, assignStudentsToAuthorizationPort, getUserPort);

    @Test
    void whenCreatedByUserDoesNotExist_thenThrowsConflict() {
        String createdBy = UUID.randomUUID().toString();
        Mockito.when(getUserPort.getUser(UUID.fromString(createdBy))).thenReturn(Optional.empty());

        assertThrows(UserNotExistException.class, () ->
                authorizationService.createAuthorization("Title", "Text", "draft", createdBy, null, null));
    }

    @Test
    void whenCreatedByUserExists_thenCreatesAuthorization() {
        String createdBy = UUID.randomUUID().toString();
        Mockito.when(getUserPort.getUser(UUID.fromString(createdBy)))
                .thenReturn(Optional.of(new User(UUID.fromString(createdBy), "Name")));

        Authorization authorization = new Authorization(UUID.randomUUID(), "Title", "Text", "draft", null, createdBy, null, null, null, null);
        Mockito.when(createAuthorizationPort.createAuthorization(any(Authorization.class))).thenReturn(authorization);

        Authorization result = authorizationService.createAuthorization("Title", "Text", "draft", createdBy, null, null);
        assertEquals(authorization, result);
    }
}

