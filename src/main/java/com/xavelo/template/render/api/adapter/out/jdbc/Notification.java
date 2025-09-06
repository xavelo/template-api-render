package com.xavelo.template.render.api.adapter.out.jdbc;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "\"notification\"")
public class Notification implements Serializable {

    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    @Column(name = "id")
    private UUID id;

    @Column(name = "authorization_id", nullable = false)
    private UUID authorizationId;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @Column(name = "guardian_id", nullable = false)
    private UUID guardianId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "responded_at")
    private Instant respondedAt;

    @Column(name = "responded_by")
    private String respondedBy;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getAuthorizationId() { return authorizationId; }
    public void setAuthorizationId(UUID authorizationId) { this.authorizationId = authorizationId; }

    public UUID getStudentId() { return studentId; }
    public void setStudentId(UUID studentId) { this.studentId = studentId; }

    public UUID getGuardianId() { return guardianId; }
    public void setGuardianId(UUID guardianId) { this.guardianId = guardianId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }

    public Instant getRespondedAt() { return respondedAt; }
    public void setRespondedAt(Instant respondedAt) { this.respondedAt = respondedAt; }

    public String getRespondedBy() { return respondedBy; }
    public void setRespondedBy(String respondedBy) { this.respondedBy = respondedBy; }
}
