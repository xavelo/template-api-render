package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface GetUserUseCase {
    Optional<User> getUser(UUID id);
}

