package com.xavelo.template.render.api.adapter.out.jdbc;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "\"authorization_student\"")
public class AuthorizationStudent implements Serializable {

    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    @Column(name = "id")
    private UUID id;

    @Column(name = "authorization_id", nullable = false)
    private UUID authorizationId;

    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAuthorizationId() {
        return authorizationId;
    }

    public void setAuthorizationId(UUID authorizationId) {
        this.authorizationId = authorizationId;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }
}

