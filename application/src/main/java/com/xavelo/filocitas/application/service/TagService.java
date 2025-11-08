package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.Quote;
import com.xavelo.filocitas.application.domain.Tag;
import com.xavelo.filocitas.port.in.GetTagsCountUseCase;
import com.xavelo.filocitas.port.out.SaveTagPort;
import com.xavelo.filocitas.port.out.LoadTagPort;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class TagService implements GetTagsCountUseCase {

    private final LoadTagPort loadTagPort;
    private final SaveTagPort saveTagPort;

    public TagService(LoadTagPort loadTagPort, SaveTagPort saveTagPort) {
        this.loadTagPort = loadTagPort;
        this.saveTagPort = saveTagPort;
    }

    @Override
    public long getTagsCount() {
        return loadTagPort.countTags();
    }

    public Quote ensureTags(Quote quote) {
        Objects.requireNonNull(quote, "quote must not be null");
        var checkedTags = checkTags(quote.getTags());
        return quote.withTags(checkedTags);
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

    public List<Tag> checkTags(List<Tag> tags) {
        if (tags == null || tags.isEmpty()) {
            return List.of();
        }

        var normalizedTags = tags.stream()
                .filter(Objects::nonNull)
                .map(tag -> new NormalizedTag(tag.getId(), normalizeName(tag.getName())))
                .filter(tag -> tag.id() != null || tag.name() != null)
                .toList();

        if (normalizedTags.isEmpty()) {
            return List.of();
        }

        var tagsById = new LinkedHashMap<UUID, Tag>();
        var tagsByName = new LinkedHashMap<String, Tag>();
        var resolved = new LinkedHashMap<String, Tag>();

        for (var normalizedTag : normalizedTags) {
            var resolvedTag = resolveTag(normalizedTag, tagsById, tagsByName);
            if (resolvedTag == null) {
                continue;
            }

            var key = resolvedTag.getId() != null ? resolvedTag.getId().toString() : normalizedTag.name();
            if (key != null) {
                resolved.putIfAbsent(key, resolvedTag);
            }
        }

        return resolved.isEmpty() ? List.of() : List.copyOf(resolved.values());
    }

    private Tag resolveTag(NormalizedTag tag,
                           Map<UUID, Tag> tagsById,
                           Map<String, Tag> tagsByName) {
        if (tag.id() != null) {
            var existing = tagsById.get(tag.id());
            if (existing == null && !tagsById.containsKey(tag.id())) {
                existing = loadTagPort.findAllByIds(List.of(tag.id())).get(tag.id());
                tagsById.put(tag.id(), existing);
                if (existing != null) {
                    var normalizedName = normalizeName(existing.getName());
                    if (tag.name() != null) {
                        tagsByName.putIfAbsent(tag.name(), existing);
                    }
                    if (normalizedName != null) {
                        tagsByName.putIfAbsent(normalizedName, existing);
                    }
                }
            }
            if (existing != null && existing.getId() != null) {
                return existing;
            }
        }

        if (tag.name() == null) {
            return null;
        }

        var existingByName = tagsByName.get(tag.name());
        if (existingByName == null && !tagsByName.containsKey(tag.name())) {
            existingByName = loadTagPort.findAllByNames(List.of(tag.name())).get(tag.name());
            tagsByName.put(tag.name(), existingByName);
            if (existingByName != null && existingByName.getId() != null) {
                tagsById.putIfAbsent(existingByName.getId(), existingByName);
            }
        }
        if (existingByName != null && existingByName.getId() != null) {
            return existingByName;
        }

        var savedTag = saveTagPort.saveTag(tag.name());
        if (savedTag != null) {
            if (savedTag.getId() != null) {
                tagsById.put(savedTag.getId(), savedTag);
            }
            tagsByName.put(tag.name(), savedTag);
        }
        return savedTag;
    }

    private record NormalizedTag(UUID id, String name) {
    }

    private String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        var trimmed = name.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
