package com.xavelo.template.render.api.application.port.out;

import com.xavelo.template.render.api.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface GetUserPort {
    Optional<User> getUser(UUID id);
}

