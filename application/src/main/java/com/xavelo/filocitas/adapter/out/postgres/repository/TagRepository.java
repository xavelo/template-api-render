package com.xavelo.filocitas.adapter.out.postgres.repository;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TagRepository extends JpaRepository<TagEntity, UUID> {

    List<TagEntity> findAllByNameIn(Collection<String> names);

    @Query("select t.name from TagEntity t order by t.name asc")
    List<String> findAllTagNames();
}
