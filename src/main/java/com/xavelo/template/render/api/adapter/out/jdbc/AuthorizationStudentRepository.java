package com.xavelo.template.render.api.adapter.out.jdbc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface AuthorizationStudentRepository extends JpaRepository<AuthorizationStudent, UUID> {
    List<AuthorizationStudent> findByAuthorizationId(UUID authorizationId);
}

