package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.out.AssignStudentsToAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.CreateAuthorizationPort;
import com.xavelo.template.render.api.application.port.out.GetUserPort;
import com.xavelo.template.render.api.application.port.out.ListAuthorizationsPort;
import com.xavelo.template.render.api.domain.Authorization;
import com.xavelo.template.render.api.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthorizationServiceTest {

    @Mock
    private CreateAuthorizationPort createAuthorizationPort;
    @Mock
    private AssignStudentsToAuthorizationPort assignStudentsToAuthorizationPort;
    @Mock
    private ListAuthorizationsPort listAuthorizationsPort;
    @Mock
    private GetUserPort getUserPort;

    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authorizationService = new AuthorizationService(createAuthorizationPort, assignStudentsToAuthorizationPort, listAuthorizationsPort, getUserPort);
    }

    @Test
    void whenCreatedByUserDoesNotExist_thenThrowsConflict() {
        String createdBy = UUID.randomUUID().toString();
        Mockito.when(getUserPort.getUser(UUID.fromString(createdBy))).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                authorizationService.createAuthorization("Title", "Text", "draft", createdBy, null, null));
        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void whenCreatedByUserExists_thenCreatesAuthorization() {
        String createdBy = UUID.randomUUID().toString();
        Mockito.when(getUserPort.getUser(UUID.fromString(createdBy))).thenReturn(Optional.of(new User(UUID.fromString(createdBy), "name")));
        Authorization authorization = new Authorization(UUID.randomUUID(), "Title", "Text", "draft", null, createdBy, null, null, null, null);
        Mockito.when(createAuthorizationPort.createAuthorization(Mockito.any())).thenReturn(authorization);

        Authorization result = authorizationService.createAuthorization("Title", "Text", "draft", createdBy, null, null);
        assertEquals(authorization, result);
    }
}
