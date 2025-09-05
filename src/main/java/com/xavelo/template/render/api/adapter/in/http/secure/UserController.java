package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.application.port.in.ListUsersUseCase;
import com.xavelo.template.render.api.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final ListUsersUseCase listUsersUseCase;

    public UserController(ListUsersUseCase listUsersUseCase) {
        this.listUsersUseCase = listUsersUseCase;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> latencySync() {
        List<User> users = listUsersUseCase.listUsers();
        return ResponseEntity.ok(users);
    }

}
