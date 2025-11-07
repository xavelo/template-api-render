package com.xavelo.filocitas.adapter.out.postgres.repository;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<AuthorEntity, UUID> {

    @Query("SELECT DISTINCT a FROM AuthorEntity a")
    List<AuthorEntity> findAllDistinct();

    Optional<AuthorEntity> findByNameIgnoreCase(String name);
}
