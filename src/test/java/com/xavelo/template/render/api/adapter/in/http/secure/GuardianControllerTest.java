package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.application.port.in.CreateGuardianUseCase;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GuardianController.class)
class GuardianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateGuardianUseCase createGuardianUseCase;

    @MockBean
    private ListGuardiansUseCase listGuardiansUseCase;

    @Test
    void whenListingGuardians_thenReturnsOk() throws Exception {
        List<Guardian> guardians = List.of(new Guardian(UUID.randomUUID(), "John"));
        when(listGuardiansUseCase.listGuardians()).thenReturn(guardians);

        mockMvc.perform(get("/api/guardians")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
