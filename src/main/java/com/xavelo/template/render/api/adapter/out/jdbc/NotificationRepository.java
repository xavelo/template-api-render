package com.xavelo.template.render.api.adapter.out.jdbc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import com.xavelo.template.render.api.domain.NotificationStatus;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByAuthorizationId(UUID authorizationId);
    List<Notification> findByStatus(NotificationStatus status);
}
