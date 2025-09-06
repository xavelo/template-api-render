package com.xavelo.template.render.api.application.port.out;

import java.util.List;
import java.util.UUID;

public interface AssignGuardiansToStudentPort {
    void assignGuardiansToStudent(UUID studentId, List<UUID> guardianIds);
}

