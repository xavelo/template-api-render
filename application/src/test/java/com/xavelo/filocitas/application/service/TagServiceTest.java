package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.Author;
import com.xavelo.filocitas.application.domain.Quote;
import com.xavelo.filocitas.application.domain.Tag;
import com.xavelo.filocitas.port.out.LoadTagPort;
import com.xavelo.filocitas.port.out.SaveTagPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private LoadTagPort loadTagPort;
    @Mock
    private SaveTagPort saveTagPort;

    private TagService tagService;

    @BeforeEach
    void setUp() {
        tagService = new TagService(loadTagPort, saveTagPort);
    }

    @Test
    void getTagsCount_shouldDelegateToLoadPort() {
        when(loadTagPort.countTags()).thenReturn(42L);

        assertThat(tagService.getTagsCount()).isEqualTo(42L);
        verify(loadTagPort).countTags();
    }

    @Test
    void checkTag_shouldRejectBlankNames() {
        assertThatThrownBy(() -> tagService.checkTag("   "))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(loadTagPort, saveTagPort);
    }

    @Test
    void checkTag_shouldReturnExistingTagWhenFound() {
        var existingTag = new Tag(UUID.randomUUID(), "Wisdom");
        when(loadTagPort.findAllByNames(any())).thenReturn(Map.of("Wisdom", existingTag));

        var result = tagService.checkTag("  Wisdom  ");

        assertThat(result).isSameAs(existingTag);
        verify(loadTagPort).findAllByNames(List.of("Wisdom"));
        verify(saveTagPort, never()).saveTag(any());
    }

    @Test
    void checkTag_shouldPersistAndReturnNewTagWhenMissing() {
        when(loadTagPort.findAllByNames(any())).thenReturn(Map.of());
        var savedTag = new Tag(UUID.randomUUID(), "Wisdom");
        when(saveTagPort.saveTag("Wisdom")).thenReturn(savedTag);

        var result = tagService.checkTag("Wisdom");

        assertThat(result).isSameAs(savedTag);
        verify(saveTagPort).saveTag("Wisdom");
    }

    @Test
    void checkTags_shouldResolveExistingAndPersistMissingTags() {
        var existingId = UUID.randomUUID();
        var existingTag = new Tag(existingId, "Stoicism");
        var tagByName = new Tag(UUID.randomUUID(), "Wisdom");
        var persistedTag = new Tag(UUID.randomUUID(), "Friendship");

        when(loadTagPort.findAllByIds(any())).thenReturn(Map.of(existingId, existingTag));
        var namesMap = new LinkedHashMap<String, Tag>();
        namesMap.put("Wisdom", tagByName);
        when(loadTagPort.findAllByNames(any())).thenReturn(namesMap);
        when(saveTagPort.saveTag("Friendship")).thenReturn(persistedTag);

        var inputTags = new java.util.ArrayList<>(List.of(
                existingTag,
                new Tag(null, "Wisdom"),
                new Tag(null, "Friendship"),
                new Tag(null, "Friendship")
        ));
        inputTags.add(null);
        inputTags.add(new Tag(null, "   "));

        var result = tagService.checkTags(inputTags);

        assertThat(result).containsExactly(existingTag, tagByName, persistedTag);
        verify(saveTagPort).saveTag("Friendship");
    }

    @Test
    void ensureTags_shouldReplaceQuoteTagsWithResolvedOnes() {
        var author = new Author("Seneca", "https://example.com/seneca");
        var originalQuote = new Quote(author, "Letters", 65, "Luck is what happens when preparation meets opportunity", List.of(new Tag("Wisdom")), "1st");
        var resolvedTag = new Tag(UUID.randomUUID(), "Wisdom");

        when(loadTagPort.findAllByIds(any())).thenReturn(Map.of());
        when(loadTagPort.findAllByNames(any())).thenReturn(Map.of("Wisdom", resolvedTag));

        var result = tagService.ensureTags(originalQuote);

        assertThat(result.getTags()).containsExactly(resolvedTag);
    }

    @Test
    void ensureTags_shouldFailFastWhenQuoteIsNull() {
        assertThatThrownBy(() -> tagService.ensureTags(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("quote must not be null");
    }
}
