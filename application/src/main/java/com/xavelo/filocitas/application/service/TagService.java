package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.Quote;
import com.xavelo.filocitas.application.domain.Tag;
import com.xavelo.filocitas.port.in.GetTagsCountUseCase;
import com.xavelo.filocitas.port.out.SaveTagPort;
import com.xavelo.filocitas.port.out.LoadTagPort;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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

        var ids = normalizedTags.stream()
                .map(NormalizedTag::id)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        var names = normalizedTags.stream()
                .map(NormalizedTag::name)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<UUID, Tag> tagsById = new LinkedHashMap<>(loadTagPort.findAllByIds(ids));
        Map<String, Tag> tagsByName = new LinkedHashMap<>(loadTagPort.findAllByNames(names));

        var resolved = new LinkedHashMap<String, Tag>();
        for (var normalizedTag : normalizedTags) {
            Tag resolvedTag = null;

            if (normalizedTag.id() != null) {
                resolvedTag = tagsById.get(normalizedTag.id());
            }

            if (resolvedTag == null && normalizedTag.name() != null) {
                resolvedTag = tagsByName.get(normalizedTag.name());
            }

            if (resolvedTag == null && normalizedTag.name() != null) {
                resolvedTag = saveTagPort.saveTag(normalizedTag.name());
                if (resolvedTag != null) {
                    if (resolvedTag.getId() != null) {
                        tagsById.putIfAbsent(resolvedTag.getId(), resolvedTag);
                    }
                    tagsByName.putIfAbsent(normalizedTag.name(), resolvedTag);
                }
            }

            if (resolvedTag != null) {
                var key = resolvedTag.getId() != null ? resolvedTag.getId().toString() : normalizedTag.name();
                if (key != null) {
                    resolved.putIfAbsent(key, resolvedTag);
                }
            }
        }

        return resolved.isEmpty() ? List.of() : List.copyOf(resolved.values());
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
