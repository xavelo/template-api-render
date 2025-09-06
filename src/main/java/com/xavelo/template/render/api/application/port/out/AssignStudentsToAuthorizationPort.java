package com.xavelo.template.render.api.application.port.out;

import java.util.List;
import java.util.UUID;

public interface AssignStudentsToAuthorizationPort {
    void assignStudentsToAuthorization(UUID authorizationId, List<UUID> studentIds);
}

