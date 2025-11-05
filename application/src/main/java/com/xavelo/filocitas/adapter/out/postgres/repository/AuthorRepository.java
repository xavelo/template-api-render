package com.xavelo.filocitas.adapter.out.postgres.repository;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<AuthorEntity, UUID> {
}
