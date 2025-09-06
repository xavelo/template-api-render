package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.CreateUserUseCase;
import com.xavelo.template.render.api.application.port.in.GetUserUseCase;
import com.xavelo.template.render.api.application.port.in.ListUsersUseCase;
import com.xavelo.template.render.api.application.port.out.CreateUserPort;
import com.xavelo.template.render.api.application.port.out.GetUserPort;
import com.xavelo.template.render.api.application.port.out.ListUsersPort;
import com.xavelo.template.render.api.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements ListUsersUseCase, CreateUserUseCase, GetUserUseCase {

    private final ListUsersPort listUsersPort;
    private final CreateUserPort createUserPort;
    private final GetUserPort getUserPort;
  
    public UserService(ListUsersPort listUsersPort, CreateUserPort createUserPort, GetUserPort getUserPort) {
        this.listUsersPort = listUsersPort;
        this.createUserPort = createUserPort;
        this.getUserPort = getUserPort;
    }

    @Override
    public List<User> listUsers() {
        return listUsersPort.listUsers();
    }

    @Override
    public User createUser(String name) {
        User user = new User(UUID.randomUUID(), name);
        return createUserPort.createUser(user);
    }

    @Override
    public Optional<User> getUser(UUID id) {
        return getUserPort.getUser(id);
    }

}

