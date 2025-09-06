package com.xavelo.template.render.api.application.port.out;

import com.xavelo.template.render.api.domain.Notification;

public interface NotificationEmailPort {
    void sendNotificationEmail(Notification notification);
}
