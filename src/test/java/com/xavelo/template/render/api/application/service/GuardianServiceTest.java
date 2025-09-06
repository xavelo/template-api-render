package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.out.CreateGuardianPort;
import com.xavelo.template.render.api.application.port.out.ListGuardiansPort;
import com.xavelo.template.render.api.domain.Guardian;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class GuardianServiceTest {

    private final CreateGuardianPort createGuardianPort = Mockito.mock(CreateGuardianPort.class);
    private final ListGuardiansPort listGuardiansPort = Mockito.mock(ListGuardiansPort.class);
    private final GuardianService guardianService = new GuardianService(createGuardianPort, listGuardiansPort);

    @Test
    void whenListingGuardians_thenReturnsFromPort() {
        List<Guardian> guardians = List.of(new Guardian(UUID.randomUUID(), "John", "john@example.com"));
        when(listGuardiansPort.listGuardians()).thenReturn(guardians);

        List<Guardian> result = guardianService.listGuardians();
        assertEquals(guardians, result);
    }
}
