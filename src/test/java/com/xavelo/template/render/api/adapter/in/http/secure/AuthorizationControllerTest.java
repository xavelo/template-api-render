package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.adapter.in.http.authorization.AuthorizationController;
import com.xavelo.template.render.api.application.port.in.AssignStudentsToAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.NotificationUseCase;
import com.xavelo.template.render.api.application.port.in.CreateAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.GetAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.ListAuthorizationsUseCase;
import com.xavelo.template.render.api.application.exception.UserNotFoundException;
import com.xavelo.template.render.api.domain.Authorization;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AuthorizationController.class)
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateAuthorizationUseCase createAuthorizationUseCase;

    @MockBean
    private AssignStudentsToAuthorizationUseCase assignStudentsToAuthorizationUseCase;

    @MockBean
    private ListAuthorizationsUseCase listAuthorizationsUseCase;

    @MockBean
    private GetAuthorizationUseCase getAuthorizationUseCase;

    @MockBean
    private NotificationUseCase notificationUseCase;

    @Test
    void whenMissingRequiredField_thenReturnsBadRequest() throws Exception {
        String json = """
                {
                  \"title\": \"Title\",
                  \"status\": \"draft\",
                  \"createdBy\": \"00000000-0000-0000-0000-000000000000\"
                }
                """;

        mockMvc.perform(post("/api/authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAllRequiredFieldsProvided_thenReturnsCreated() throws Exception {
        Authorization authorization = new Authorization(UUID.randomUUID(), "Title", "Text", "draft", Instant.now(), "00000000-0000-0000-0000-000000000000", null, null, null, null, List.of());
        Mockito.when(createAuthorizationUseCase.createAuthorization(any(), any(), any(), any(), any(), any()))
                .thenReturn(authorization);

        String json = """
                {
                  \"title\": \"Title\",
                  \"text\": \"Text\",
                  \"status\": \"draft\",
                  \"createdBy\": \"00000000-0000-0000-0000-000000000000\"
                }
                """;

        mockMvc.perform(post("/api/authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void whenCreatedByInvalidUuid_thenReturnsBadRequest() throws Exception {
        String json = """
                {
                  \"title\": \"Title\",
                  \"text\": \"Text\",
                  \"status\": \"draft\",
                  \"createdBy\": \"not-a-uuid\"
                }
                """;

        mockMvc.perform(post("/api/authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCreatedByUserDoesNotExist_thenReturnsConflict() throws Exception {
        Mockito.when(createAuthorizationUseCase.createAuthorization(any(), any(), any(), any(), any(), any()))
                .thenThrow(new UserNotFoundException(UUID.randomUUID()));

        String json = """
                {
                  \"title\": \"Title\",
                  \"text\": \"Text\",
                  \"status\": \"draft\",
                  \"createdBy\": \"00000000-0000-0000-0000-000000000000\"
                }
                """;

        mockMvc.perform(post("/api/authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    void whenAssigningStudents_thenReturnsCreated() throws Exception {
        UUID authorizationId = UUID.randomUUID();
        String json = """
                {
                  \"studentIds\": [\"00000000-0000-0000-0000-000000000000\"]
                }
                """;

        mockMvc.perform(post("/api/authorization/" + authorizationId + "/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        Mockito.verify(assignStudentsToAuthorizationUseCase)
                .assignStudents(Mockito.eq(authorizationId), Mockito.any());
    }

    @Test
    void whenListingAuthorizations_thenReturnsOk() throws Exception {
        Authorization authorization = new Authorization(UUID.randomUUID(), "Title", "Text", "draft", Instant.now(), "user1", null, null, null, null, List.of());
        Mockito.when(listAuthorizationsUseCase.listAuthorizations()).thenReturn(List.of(authorization));

        mockMvc.perform(get("/api/authorizations"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGettingAuthorization_thenReturnsStudents() throws Exception {
        UUID authorizationId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        Authorization authorization = new Authorization(authorizationId, "Title", "Text", "draft", Instant.now(), "user1", null, null, null, null, List.of(studentId));
        Mockito.when(getAuthorizationUseCase.getAuthorization(authorizationId)).thenReturn(java.util.Optional.of(authorization));

        mockMvc.perform(get("/api/authorization/" + authorizationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentIds[0]").value(studentId.toString()));
    }
}
