package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.application.port.in.AssignStudentsToAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.CreateAuthorizationUseCase;
import com.xavelo.template.render.api.domain.Authorization;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorizationController.class)
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateAuthorizationUseCase createAuthorizationUseCase;

    @MockBean
    private AssignStudentsToAuthorizationUseCase assignStudentsToAuthorizationUseCase;

    @Test
    void whenMissingRequiredField_thenReturnsBadRequest() throws Exception {
        String json = """
                {
                  "title": "Title",
                  "status": "draft",
                  "createdBy": "user1"
                }
                """;

        mockMvc.perform(post("/api/authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAllRequiredFieldsProvided_thenReturnsCreated() throws Exception {
        Authorization authorization = new Authorization(UUID.randomUUID(), "Title", "Text", "draft", Instant.now(), "user1", null, null, null, null);
        Mockito.when(createAuthorizationUseCase.createAuthorization(any(), any(), any(), any(), any(), any()))
                .thenReturn(authorization);

        String json = """
                {
                  "title": "Title",
                  "text": "Text",
                  "status": "draft",
                  "createdBy": "user1"
                }
                """;

        mockMvc.perform(post("/api/authorization")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void whenAssigningStudents_thenReturnsCreated() throws Exception {
        UUID authorizationId = UUID.randomUUID();
        String json = """
                {
                  "studentIds": ["00000000-0000-0000-0000-000000000000"]
                }
                """;

        mockMvc.perform(post("/api/authorization/" + authorizationId + "/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        Mockito.verify(assignStudentsToAuthorizationUseCase)
                .assignStudents(Mockito.eq(authorizationId), Mockito.any());
    }
}
