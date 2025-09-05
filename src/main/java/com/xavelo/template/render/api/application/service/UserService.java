package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.ListUsersUseCase;
import com.xavelo.template.render.api.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements ListUsersUseCase {

    @Override
    public List<User> listUsers() {
        return List.of();
    }

}
