package com.xavelo.template.render.api.adapter.out.email;

import com.xavelo.template.render.api.application.port.out.NotificationEmailPort;
import com.xavelo.template.render.api.domain.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(JavaMailSender.class)
public class EmailAdapter implements NotificationEmailPort {

    private static final Logger logger = LoggerFactory.getLogger(EmailAdapter.class);

    private final JavaMailSender mailSender;

    public EmailAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendNotificationEmail(Notification notification) {

    }
}