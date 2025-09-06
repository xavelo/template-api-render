package com.xavelo.template.render.api.adapter.in.http.authorization;

import com.xavelo.template.render.api.domain.NotificationStatus;

public record RespondNotificationRequest(NotificationStatus status, String respondedBy) {}
