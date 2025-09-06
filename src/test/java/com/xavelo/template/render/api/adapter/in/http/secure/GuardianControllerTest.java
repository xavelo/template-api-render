package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.application.port.in.CreateGuardianUseCase;
import com.xavelo.template.render.api.application.port.in.GetGuardianUseCase;
import com.xavelo.template.render.api.application.port.in.ListGuardiansUseCase;
import com.xavelo.template.render.api.domain.Guardian;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GuardianController.class)
class GuardianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateGuardianUseCase createGuardianUseCase;

    @MockBean
    private ListGuardiansUseCase listGuardiansUseCase;

    @MockBean
    private GetGuardianUseCase getGuardianUseCase;

    @Test
    void whenListingGuardians_thenReturnsOk() throws Exception {
        List<Guardian> guardians = List.of(new Guardian(UUID.randomUUID(), "John", "john@example.com"));
        when(listGuardiansUseCase.listGuardians()).thenReturn(guardians);

        mockMvc.perform(get("/api/guardians")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenCreatingGuardianWithEmail_thenReturnsCreated() throws Exception {
        Guardian guardian = new Guardian(UUID.randomUUID(), "John", "john@example.com");
        when(createGuardianUseCase.createGuardian("John", "john@example.com")).thenReturn(guardian);

        String json = "{ \"name\": \"John\", \"email\": \"john@example.com\" }";
        mockMvc.perform(post("/api/guardian")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void whenCreatingGuardianWithoutEmail_thenReturnsBadRequest() throws Exception {
        String json = "{ \"name\": \"John\" }";
        mockMvc.perform(post("/api/guardian")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}
