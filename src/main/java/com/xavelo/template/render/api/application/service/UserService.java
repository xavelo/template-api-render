package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.CreateUserUseCase;
import com.xavelo.template.render.api.application.port.in.ListUsersUseCase;
import com.xavelo.template.render.api.application.port.out.CreateUserPort;
import com.xavelo.template.render.api.application.port.out.ListUsersPort;
import com.xavelo.template.render.api.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements ListUsersUseCase, CreateUserUseCase {

    private final ListUsersPort listUsersPort;
    private final CreateUserPort createUserPort;

    public UserService(ListUsersPort listUsersPort, CreateUserPort createUserPort) {
        this.listUsersPort = listUsersPort;
        this.createUserPort = createUserPort;
    }

    @Override
    public List<User> listUsers() {
        return listUsersPort.listUsers();
    }

    @Override
    public User createUser(User user) {
        return createUserPort.createUser(user);
    }

}
