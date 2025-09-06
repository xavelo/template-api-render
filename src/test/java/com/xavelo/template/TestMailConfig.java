package com.xavelo.template;

import com.xavelo.template.render.api.application.port.out.NotificationEmailPort;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration
public class TestMailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        return Mockito.mock(JavaMailSender.class);
    }

    @Bean
    public NotificationEmailPort notificationEmailPort() {
        return Mockito.mock(NotificationEmailPort.class);
    }
}
