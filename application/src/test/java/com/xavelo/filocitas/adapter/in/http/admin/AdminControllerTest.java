package com.xavelo.filocitas.adapter.in.http.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xavelo.filocitas.adapter.in.http.exception.ApiExceptionHandler;
import com.xavelo.filocitas.adapter.in.http.mapper.ApiMapper;
import com.xavelo.filocitas.api.model.QuoteRequest;
import com.xavelo.filocitas.application.domain.Author;
import com.xavelo.filocitas.application.domain.Quote;
import com.xavelo.filocitas.application.exception.QuoteAlreadyExistsException;
import com.xavelo.filocitas.port.in.DeleteQuoteUseCase;
import com.xavelo.filocitas.port.in.ExportQuotesUseCase;
import com.xavelo.filocitas.port.in.GetAuthorsQuotesCountUseCase;
import com.xavelo.filocitas.port.in.SaveUquoteUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class)
@Import(ApiExceptionHandler.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SaveUquoteUseCase saveUquoteUseCase;

    @MockBean
    private DeleteQuoteUseCase deleteQuoteUseCase;

    @MockBean
    private GetAuthorsQuotesCountUseCase getAuthorsQuotesCountUseCase;

    @MockBean
    private ExportQuotesUseCase exportQuotesUseCase;

    @MockBean
    private ApiMapper apiMapper;

    @Test
    void shouldReturnConflictWhenQuoteAlreadyExists() throws Exception {
        var requestPayload = new QuoteRequest();
        requestPayload.setAuthor("Sócrates");
        requestPayload.setQuote("Solo sé que no sé nada.");
        requestPayload.setTags(List.of());

        var domainQuote = new Quote(
                new Author("Sócrates", null),
                null,
                null,
                "Solo sé que no sé nada.",
                List.of(),
                null
        );

        when(apiMapper.toDomainQuote(any(QuoteRequest.class))).thenReturn(domainQuote);
        when(saveUquoteUseCase.saveQuote(any(Quote.class), any(String.class)))
                .thenThrow(new QuoteAlreadyExistsException("Quote already exists: Solo sé que no sé nada."));

        mockMvc.perform(post("/api/admin/quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPayload)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Quote already exists: Solo sé que no sé nada."))
                .andExpect(jsonPath("$.timestamp").exists());

        Mockito.verify(saveUquoteUseCase).saveQuote(any(Quote.class), any(String.class));
    }
}

