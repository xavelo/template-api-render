package com.xavelo.template.render.api.adapter.out.email;

import com.xavelo.template.render.api.application.port.out.NotificationEmailPort;
import com.xavelo.template.render.api.application.port.out.SendEmailPort;
import com.xavelo.template.render.api.application.port.out.GetGuardianPort;
import com.xavelo.template.render.api.domain.Notification;
import com.xavelo.template.render.api.domain.Guardian;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnBean(SendEmailPort.class)
@ConditionalOnMissingBean(NotificationEmailPort.class)
public class NotificationEmailAdapter implements NotificationEmailPort {

    private static final Logger logger = LoggerFactory.getLogger(NotificationEmailAdapter.class);

    private final SendEmailPort sendEmailPort;
    private final GetGuardianPort getGuardianPort;

    public NotificationEmailAdapter(SendEmailPort sendEmailPort, GetGuardianPort getGuardianPort) {
        this.sendEmailPort = sendEmailPort;
        this.getGuardianPort = getGuardianPort;
    }

    @Override
    public void sendNotificationEmail(Notification notification) {
        Optional<Guardian> guardian = getGuardianPort.getGuardian(notification.guardianId());
        if (guardian.isEmpty()) {
            logger.warn("Guardian with id {} not found. Email not sent.", notification.guardianId());
            return;
        }

        String to = guardian.get().email();
        String subject = "Authorization Notification";
        String content = "You have a new notification for authorization " + notification.authorizationId();
        sendEmailPort.sendEmail("no-reply@example.com", to, subject, content);
        logger.info("Sent notification email to {} for authorization {}", to, notification.authorizationId());
    }
}

