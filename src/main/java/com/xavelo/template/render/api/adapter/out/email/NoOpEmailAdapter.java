package com.xavelo.template.render.api.adapter.out.email;

import com.xavelo.template.render.api.application.port.out.NotificationEmailPort;
import com.xavelo.template.render.api.domain.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnMissingBean(JavaMailSender.class)
public class NoOpEmailAdapter implements NotificationEmailPort {

    private static final Logger logger = LoggerFactory.getLogger(NoOpEmailAdapter.class);

    @Override
    public void sendNotificationEmail(Notification notification) {
        logger.info("NoOpEmailAdapter - email not sent: {}", notification);
    }
}
