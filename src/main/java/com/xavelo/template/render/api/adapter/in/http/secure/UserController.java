package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.application.port.in.CreateUserUseCase;
import com.xavelo.template.render.api.application.port.in.ListUsersUseCase;
import com.xavelo.template.render.api.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final ListUsersUseCase listUsersUseCase;
    private final CreateUserUseCase createUserUseCase;

    public UserController(ListUsersUseCase listUsersUseCase, CreateUserUseCase createUserUseCase) {
        this.listUsersUseCase = listUsersUseCase;
        this.createUserUseCase = createUserUseCase;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> latencySync() {
        List<User> users = listUsersUseCase.listUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = createUserUseCase.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

}
