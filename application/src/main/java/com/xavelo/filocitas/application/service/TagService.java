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

        var filteredTags = tags.stream()
                .filter(Objects::nonNull)
                .toList();

        if (filteredTags.isEmpty()) {
            return List.of();
        }

        var ids = filteredTags.stream()
                .map(Tag::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<UUID, Tag> resolvedById = new LinkedHashMap<>(loadTagPort.findAllByIds(ids));

        var resolvedByName = new LinkedHashMap<String, Tag>();
        var resolvedByKey = new LinkedHashMap<String, Tag>();
        Map<UUID, Tag> tagsById = new LinkedHashMap<>(loadTagPort.findTagsByIds(ids));
        Map<String, Tag> tagsByName = new LinkedHashMap<>(loadTagPort.findAllByNames(names));

        for (var tag : filteredTags) {
            Tag resolvedTag = null;

            var tagId = tag.getId();
            var normalizedName = normalizeName(tag.getName());

            if (tagId != null) {
                resolvedTag = resolvedById.get(tagId);
                if (resolvedTag != null && normalizedName != null) {
                    resolvedByName.putIfAbsent(normalizedName, resolvedTag);
                }
            }

            if (resolvedTag == null && normalizedName != null) {
                resolvedTag = resolvedByName.get(normalizedName);

                if (resolvedTag == null) {
                    resolvedTag = checkTag(normalizedName);
                    if (resolvedTag != null) {
                        if (resolvedTag.getId() != null) {
                            resolvedById.putIfAbsent(resolvedTag.getId(), resolvedTag);
                        }

                        var resolvedNameKey = normalizeName(resolvedTag.getName());
                        if (resolvedNameKey != null) {
                            resolvedByName.putIfAbsent(resolvedNameKey, resolvedTag);
                        }

                        resolvedByName.putIfAbsent(normalizedName, resolvedTag);
                    }
                }
            }

            if (resolvedTag == null) {
                continue;
            }

            var key = resolvedTag.getId() != null
                    ? resolvedTag.getId().toString()
                    : (normalizedName != null ? normalizedName : normalizeName(resolvedTag.getName()));

            if (key == null) {
                continue;
            }

            resolvedByKey.putIfAbsent(key, resolvedTag);
        }

        return resolvedByKey.isEmpty() ? List.of() : List.copyOf(resolvedByKey.values());
    }

    private String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        var trimmed = name.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
