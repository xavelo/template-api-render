package com.xavelo.template.render.api.application.port.in;

import java.util.List;
import java.util.UUID;

public interface AssignGuardiansToStudentUseCase {
    void assignGuardians(UUID studentId, List<UUID> guardianIds);
}

