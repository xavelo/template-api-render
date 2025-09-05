package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.User;

public interface CreateUserUseCase {
    User createUser(User user);
}

