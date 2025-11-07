package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.Quote;
import com.xavelo.filocitas.application.domain.Tag;
import com.xavelo.filocitas.port.out.SaveTagPort;
import com.xavelo.filocitas.port.out.LoadTagPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final LoadTagPort loadTagPort;
    private final SaveTagPort saveTagPort;

    public TagService(LoadTagPort loadTagPort, SaveTagPort saveTagPort) {
        this.loadTagPort = loadTagPort;
        this.saveTagPort = saveTagPort;
    }

    public Quote ensureTags(Quote quote) {
        Objects.requireNonNull(quote, "quote must not be null");
        var resolvedTags = resolveTags(quote.getTags());
        return quote.withTags(resolvedTags);
    }

    public Tag checkTag(String name) {
        var normalizedName = normalizeName(name);
        if (normalizedName == null) {
            throw new IllegalArgumentException("Tag name must not be blank");
        }

        var existingTags = loadTagPort.findAllByNames(List.of(normalizedName));
        var resolved = existingTags.get(normalizedName);
        if (resolved != null) {
            return resolved;
        }

        return saveTagPort.saveTag(normalizedName);
    }

    public List<Tag> resolveTags(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }

        var filteredTags = new ArrayList<Tag>();
        for (Tag tag : tags) {
            if (tag == null) {
                continue;
            }
            var normalizedName = normalizeName(tag.getName());
            if (tag.getId() == null && normalizedName == null) {
                continue;
            }
            filteredTags.add(tag);
        }

        if (filteredTags.isEmpty()) {
            return List.of();
        }

        var ids = filteredTags.stream()
                .map(Tag::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        var names = filteredTags.stream()
                .map(tag -> normalizeName(tag.getName()))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<UUID, Tag> tagsById = new LinkedHashMap<>(loadTagPort.findAllByIds(ids));
        Map<String, Tag> tagsByName = new LinkedHashMap<>(loadTagPort.findAllByNames(names));

        var resolved = new LinkedHashMap<String, Tag>();
        for (Tag tag : filteredTags) {
            Tag resolvedTag = null;
            var id = tag.getId();
            if (id != null) {
                resolvedTag = tagsById.get(id);
            }
            var normalizedName = normalizeName(tag.getName());
            if (resolvedTag == null && normalizedName != null) {
                resolvedTag = tagsByName.get(normalizedName);
            }
            if (resolvedTag == null && normalizedName != null) {
                resolvedTag = saveTagPort.saveTag(normalizedName);
                if (resolvedTag != null) {
                    if (resolvedTag.getId() != null) {
                        tagsById.putIfAbsent(resolvedTag.getId(), resolvedTag);
                    }
                    tagsByName.putIfAbsent(normalizedName, resolvedTag);
                }
            }
            if (resolvedTag != null) {
                var key = resolvedTag.getId() != null ? resolvedTag.getId().toString() : normalizedName;
                if (key != null) {
                    resolved.putIfAbsent(key, resolvedTag);
                }
            }
        }

        return resolved.isEmpty() ? List.of() : List.copyOf(resolved.values());
    }

    private String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        var trimmed = name.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
