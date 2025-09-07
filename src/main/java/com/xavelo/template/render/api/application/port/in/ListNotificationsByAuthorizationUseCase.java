package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Notification;
import java.util.List;
import java.util.UUID;

public interface ListNotificationsByAuthorizationUseCase {
    List<Notification> listNotifications(UUID authorizationId);
}
