package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.GetUserUseCase;
import com.xavelo.template.render.api.application.port.in.ListUsersUseCase;
import com.xavelo.template.render.api.application.port.out.GetUserPort;
import com.xavelo.template.render.api.application.port.out.ListUsersPort;
import com.xavelo.template.render.api.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements ListUsersUseCase, GetUserUseCase {

    private final ListUsersPort listUsersPort;
    private final GetUserPort getUserPort;

    public UserService(ListUsersPort listUsersPort, GetUserPort getUserPort) {
        this.listUsersPort = listUsersPort;
        this.getUserPort = getUserPort;
    }

    @Override
    public List<User> listUsers() {
        return listUsersPort.listUsers();
    }

    @Override
    public Optional<User> getUser(UUID id) {
        return getUserPort.getUser(id);
    }
}

