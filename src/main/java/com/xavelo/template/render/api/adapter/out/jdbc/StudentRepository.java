package com.xavelo.template.render.api.adapter.out.jdbc;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    @Override
    @EntityGraph(attributePaths = "guardians")
    List<Student> findAll();
}
