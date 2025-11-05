package com.xavelo.filocitas.adapter.out.postgres.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tag")
public class TagEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 24, unique = true)
    private String name;

    protected TagEntity() {
        // JPA
    }

    private TagEntity(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public static TagEntity newInstance(String name) {
        return new TagEntity(name);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TagEntity tagEntity = (TagEntity) o;
        return Objects.equals(id, tagEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
