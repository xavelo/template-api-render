package com.xavelo.template;

import com.xavelo.template.render.api.adapter.out.jdbc.PostgresAdapter;
import com.xavelo.template.render.api.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TemplateApiRenderApplicationTests {

    @Autowired
    private PostgresAdapter postgresAdapter;

    @Test
    void contextLoadsAndUsesInMemoryDb() {
        User created = postgresAdapter.createUser(new User(null, "John"));
        assertNotNull(created.id());
        assertTrue(postgresAdapter.getUser(created.id()).isPresent());
    }

}
