package com.xavelo.filocitas.adapter.out.postgres.repository;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.RawQuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RawQuoteRepository extends JpaRepository<RawQuoteEntity, UUID> {
}
