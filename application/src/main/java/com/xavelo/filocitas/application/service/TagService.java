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
        var checkedTags = new ArrayList<Tag>();
        for (Tag tag : tags) {
            if (tag == null) {
                continue;
            }
            var normalizedName = normalizeName(tag.getName());
            if (tag.getId() == null && normalizedName == null) {
                continue;
            }
            checkedTags.add(tag);
        }

        var ids = checkedTags.stream()
                .map(Tag::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        var names = checkedTags.stream()
                .map(tag -> normalizeName(tag.getName()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<UUID, Tag> tagsById = new LinkedHashMap<>(loadTagPort.findAllByIds(ids));
        Map<String, Tag> tagsByName = new LinkedHashMap<>(loadTagPort.findAllByNames(names));

        var resolved = new LinkedHashMap<String, Tag>();
        for (Tag tag : checkedTags) {
            Tag checkedTag = null;
            var id = tag.getId();
            if (id != null) {
                checkedTag = tagsById.get(id);
            }
            var normalizedName = normalizeName(tag.getName());
            if (checkedTag == null && normalizedName != null) {
                checkedTag = tagsByName.get(normalizedName);
            }
            if (checkedTag == null && normalizedName != null) {
                checkedTag = saveTagPort.saveTag(normalizedName);
                if (checkedTag != null) {
                    if (checkedTag.getId() != null) {
                        tagsById.putIfAbsent(checkedTag.getId(), checkedTag);
                    }
                    tagsByName.putIfAbsent(normalizedName, checkedTag);
                }
            }
            if (checkedTag != null) {
                var key = checkedTag.getId() != null ? checkedTag.getId().toString() : normalizedName;
                if (key != null) {
                    resolved.putIfAbsent(key, checkedTag);
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
