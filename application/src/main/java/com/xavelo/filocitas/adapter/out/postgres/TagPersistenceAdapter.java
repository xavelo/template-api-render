package com.xavelo.filocitas.adapter.out.postgres;

import com.xavelo.filocitas.adapter.out.postgres.mapper.TagMapper;
import com.xavelo.filocitas.adapter.out.postgres.repository.TagRepository;
import com.xavelo.filocitas.adapter.out.postgres.repository.entity.TagEntity;
import com.xavelo.filocitas.application.domain.Tag;
import com.xavelo.filocitas.port.out.TagPersistencePort;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TagPersistenceAdapter implements TagPersistencePort {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagPersistenceAdapter(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public Map<UUID, Tag> findAllByIds(Collection<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }

        return tagRepository.findAllById(ids).stream()
                .map(tagMapper::toDomain)
                .filter(Objects::nonNull)
                .filter(tag -> tag.getId() != null)
                .collect(Collectors.toMap(
                        Tag::getId,
                        tag -> tag,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<String, Tag> findAllByNames(Collection<String> names) {
        if (names == null || names.isEmpty()) {
            return Map.of();
        }

        var normalizedNames = names.stream()
                .map(this::normalizeName)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (normalizedNames.isEmpty()) {
            return Map.of();
        }

        return tagRepository.findAllByNameIn(normalizedNames).stream()
                .map(tagMapper::toDomain)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        tag -> normalizeName(tag.getName()),
                        tag -> tag,
                        (left, right) -> left,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Tag create(String name) {
        var normalizedName = normalizeName(name);
        if (normalizedName == null) {
            throw new IllegalArgumentException("Tag name must not be blank");
        }
        TagEntity entity = tagRepository.save(TagEntity.newInstance(normalizedName));
        return tagMapper.toDomain(entity);
    }

    private String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        var trimmed = name.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
