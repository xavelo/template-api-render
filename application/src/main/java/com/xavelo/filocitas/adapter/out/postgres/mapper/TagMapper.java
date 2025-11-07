package com.xavelo.filocitas.adapter.out.postgres.mapper;

import com.xavelo.filocitas.adapter.out.postgres.repository.entity.TagEntity;
import com.xavelo.filocitas.application.domain.Tag;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public Tag toDomain(TagEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Tag(entity.getId(), entity.getName());
    }

    public List<Tag> toDomainList(Collection<TagEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }
        return entities.stream()
                .filter(Objects::nonNull)
                .map(this::toDomain)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Tag::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .collect(Collectors.toList());
    }
}
