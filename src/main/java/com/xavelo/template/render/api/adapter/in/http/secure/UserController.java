package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.application.port.in.GetUserUseCase;
import com.xavelo.template.render.api.application.port.in.ListUsersUseCase;
import com.xavelo.template.render.api.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final ListUsersUseCase listUsersUseCase;
    private final GetUserUseCase getUserUseCase;

    public UserController(ListUsersUseCase listUsersUseCase, GetUserUseCase getUserUseCase) {
        this.listUsersUseCase = listUsersUseCase;
        this.getUserUseCase = getUserUseCase;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = listUsersUseCase.listUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        return getUserUseCase.getUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

