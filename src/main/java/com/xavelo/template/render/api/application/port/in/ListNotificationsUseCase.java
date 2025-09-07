package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Notification;
import java.util.List;

public interface ListNotificationsUseCase {
    List<Notification> listNotifications();
}
