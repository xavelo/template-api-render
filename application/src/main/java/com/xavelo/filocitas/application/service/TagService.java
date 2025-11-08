package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.Quote;
import com.xavelo.filocitas.application.domain.Tag;
import com.xavelo.filocitas.port.in.GetTagsCountUseCase;
import com.xavelo.filocitas.port.out.SaveTagPort;
import com.xavelo.filocitas.port.out.LoadTagPort;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

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

        var resolvedByKey = new LinkedHashMap<String, Tag>();

        for (var tag : tags) {
            if (tag == null || tag.getName() == null) {
                continue;
            }

            var resolvedTag = checkTag(tag.getName());

            if (resolvedTag == null) {
                continue;
            }

            var key = resolvedTag.getId() != null
                    ? resolvedTag.getId().toString()
                    : normalizeName(resolvedTag.getName());

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
