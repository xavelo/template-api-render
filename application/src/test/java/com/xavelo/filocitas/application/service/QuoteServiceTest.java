package com.xavelo.filocitas.application.service;

import com.xavelo.filocitas.application.domain.Author;
import com.xavelo.filocitas.application.domain.Quote;
import com.xavelo.filocitas.application.domain.Tag;
import com.xavelo.filocitas.application.exception.DuplicatedQuoteException;
import com.xavelo.filocitas.port.out.DeleteQuotePort;
import com.xavelo.filocitas.port.out.LikeQuotePort;
import com.xavelo.filocitas.port.out.LoadQuotePort;
import com.xavelo.filocitas.port.out.SaveQuotePort;
import com.xavelo.filocitas.port.out.SaveRawQuotePort;
import com.xavelo.filocitas.port.out.LoadRawQuotePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

    @Mock
    private SaveQuotePort saveQuotePort;
    @Mock
    private LoadQuotePort loadQuotePort;
    @Mock
    private DeleteQuotePort deleteQuotePort;
    @Mock
    private LikeQuotePort likeQuotePort;
    @Mock
    private TagService tagService;
    @Mock
    private SaveRawQuotePort saveRawQuotePort;
    @Mock
    private LoadRawQuotePort loadRawQuotePort;

    private QuoteService quoteService;

    private Author author;

    @BeforeEach
    void setUp() {
        quoteService = new QuoteService(
                saveQuotePort,
                loadQuotePort,
                deleteQuotePort,
                likeQuotePort,
                tagService,
                saveRawQuotePort,
                loadRawQuotePort
        );
        author = new Author("Marcus Aurelius", "https://example.com/marcus");
    }

    @Test
    void saveQuote_shouldPersistQuoteAndRawPayload() {
        var originalTag = new Tag("Stoicism");
        var normalizedTags = List.of(new Tag(UUID.randomUUID(), "Stoicism"));
        when(tagService.checkTags(any())).thenReturn(normalizedTags);

        var quote = new Quote(author, "Meditations", 180, "You have power over your mind", List.of(originalTag), "2nd");
        var savedQuote = quote.withTags(normalizedTags).withId(UUID.randomUUID());
        when(saveQuotePort.saveQuote(any())).thenReturn(savedQuote);

        var rawPayload = "{\"quote\":\"You have power over your mind\"}";
        var result = quoteService.saveQuote(quote, rawPayload);

        assertThat(result).isSameAs(savedQuote);

        verify(tagService).checkTags(quote.getTags());

        var captor = ArgumentCaptor.forClass(Quote.class);
        verify(saveQuotePort).saveQuote(captor.capture());
        assertThat(captor.getValue().getTags()).containsExactlyElementsOf(normalizedTags);

        verify(saveRawQuotePort).saveRawQuote(savedQuote.getId(), rawPayload);
    }

    @Test
    void saveQuote_whenDuplicate_shouldEnrichExceptionWithQuoteAndPayload() {
        when(tagService.checkTags(any())).thenReturn(List.of());

        var quote = new Quote(author, "Letters", 65, "Persevere and preserve yourselves", List.of(), "1st");
        var rawPayload = "{\"quote\":\"Persevere and preserve yourselves\"}";
        var duplicate = new DuplicatedQuoteException(null, new RuntimeException("duplicate"));
        when(saveQuotePort.saveQuote(any())).thenThrow(duplicate);

        assertThatThrownBy(() -> quoteService.saveQuote(quote, rawPayload))
                .isInstanceOf(DuplicatedQuoteException.class)
                .satisfies(thrown -> {
                    var exception = (DuplicatedQuoteException) thrown;
                    assertThat(exception.getQuoteText()).isEqualTo(quote.getQuote());
                    assertThat(exception.getPayload()).isEqualTo(rawPayload);
                });

        verify(saveRawQuotePort, never()).saveRawQuote(any(), any());
    }

    @Test
    void saveQuotes_shouldPersistBatchAndRawPayloads() {
        when(tagService.ensureTags(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var quoteOne = new Quote(author, "Meditations", 170, "The happiness of your life depends upon the quality of your thoughts", List.of(), "2nd");
        var quoteTwo = new Quote(author, "Meditations", 175, "You have power over your mind", List.of(), "2nd");

        var savedQuoteOne = quoteOne.withId(UUID.randomUUID());
        var savedQuoteTwo = quoteTwo.withId(UUID.randomUUID());
        when(saveQuotePort.saveQuotes(any())).thenReturn(List.of(savedQuoteOne, savedQuoteTwo));

        var rawPayloads = List.of("payload-one", "payload-two");
        var result = quoteService.saveQuotes(List.of(quoteOne, quoteTwo), rawPayloads);

        assertThat(result).containsExactly(savedQuoteOne, savedQuoteTwo);

        verify(tagService).ensureTags(quoteOne);
        verify(tagService).ensureTags(quoteTwo);

        var mapCaptor = ArgumentCaptor.forClass(Map.class);
        verify(saveRawQuotePort).saveRawQuotes(mapCaptor.capture());
        assertThat(mapCaptor.getValue())
                .hasSize(2)
                .containsEntry(savedQuoteOne.getId(), "payload-one")
                .containsEntry(savedQuoteTwo.getId(), "payload-two");
    }

    @Test
    void saveQuotes_whenDuplicate_shouldSurfaceDuplicateTextAndPayload() {
        when(tagService.ensureTags(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var duplicateText = "Waste no more time arguing what a good man should be. Be one.";
        var firstQuote = new Quote(author, "Meditations", 175, duplicateText, List.of(), "2nd");
        var secondQuote = new Quote(author, "Meditations", 175, duplicateText, List.of(), "2nd");
        var rawPayloads = List.of("first", "second");

        var duplicate = new DuplicatedQuoteException(null, new RuntimeException("duplicate"));
        when(saveQuotePort.saveQuotes(any())).thenThrow(duplicate);

        assertThatThrownBy(() -> quoteService.saveQuotes(List.of(firstQuote, secondQuote), rawPayloads))
                .isInstanceOf(DuplicatedQuoteException.class)
                .satisfies(thrown -> {
                    var exception = (DuplicatedQuoteException) thrown;
                    assertThat(exception.getQuoteText()).isEqualTo(duplicateText);
                    assertThat(exception.getPayload()).isEqualTo("first");
                });

        verify(saveRawQuotePort, never()).saveRawQuotes(any());
    }

    @Test
    void getTopQuotes_withNonPositiveLimitReturnsEmptyList() {
        var result = quoteService.getTopQuotes(0);

        assertThat(result).isEmpty();
        verify(loadQuotePort, never()).findTopQuotesByLikes(anyInt());
    }

    @Test
    void likeQuote_shouldReturnIncrementedLikesWhenQuoteExists() {
        var quoteId = UUID.randomUUID();
        var storedQuote = new Quote(quoteId, author, "Meditations", 175, "Dwell on the beauty of life", List.of(), "2nd", 10);

        when(loadQuotePort.findQuoteById(quoteId)).thenReturn(Optional.of(storedQuote));
        when(likeQuotePort.incrementQuoteLike(quoteId)).thenReturn(11L);

        var result = quoteService.likeQuote(quoteId);

        assertThat(result).contains(11L);
        verify(likeQuotePort).incrementQuoteLike(quoteId);
    }

    @Test
    void likeQuote_shouldReturnEmptyWhenQuoteDoesNotExist() {
        var quoteId = UUID.randomUUID();
        when(loadQuotePort.findQuoteById(quoteId)).thenReturn(Optional.empty());

        var result = quoteService.likeQuote(quoteId);

        assertThat(result).isEmpty();
        verify(likeQuotePort, never()).incrementQuoteLike(any());
    }
}
