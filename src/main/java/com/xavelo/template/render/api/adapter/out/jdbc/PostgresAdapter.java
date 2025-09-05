package com.xavelo.template.render.api.adapter.out.jdbc;

import com.xavelo.template.render.api.application.port.out.CreateUserPort;
import com.xavelo.template.render.api.application.port.out.ListUsersPort;
import com.xavelo.template.render.api.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostgresAdapter implements ListUsersPort, CreateUserPort {

    private static final Logger logger = LoggerFactory.getLogger(PostgresAdapter.class);

    private final UserRepository userRepository;

    public PostgresAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> listUsers() {
        logger.debug("postgress query...");

        return userRepository.findAll().stream()
                .map(user -> new User(user.getId(), user.getName()))
                .toList();
    }

    @Override
    public User createUser(User user) {
        logger.debug("postgress insert...");

        com.xavelo.template.render.api.adapter.out.jdbc.User userEntity =
                new com.xavelo.template.render.api.adapter.out.jdbc.User();
        userEntity.setName(user.name());

        com.xavelo.template.render.api.adapter.out.jdbc.User savedUser = userRepository.save(userEntity);

        return new User(savedUser.getId(), savedUser.getName());
    }

}
