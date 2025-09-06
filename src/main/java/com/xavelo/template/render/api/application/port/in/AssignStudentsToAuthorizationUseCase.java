package com.xavelo.template.render.api.application.port.in;

import java.util.List;
import java.util.UUID;

public interface AssignStudentsToAuthorizationUseCase {
    void assignStudents(UUID authorizationId, List<UUID> studentIds);
}

