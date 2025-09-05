package com.xavelo.template.render.api.adapter.out.jdbc;

import com.xavelo.template.render.api.application.port.out.ListUsersPort;
import com.xavelo.template.render.api.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class PostgresAdapter implements ListUsersPort {

    private static final Logger logger = LoggerFactory.getLogger(PostgresAdapter.class);

    @Override
    public List<User> listUsers() {
        logger.debug("postgress query...");
        return List.of(new User(UUID.randomUUID(), "Pepe"));
    }

}
