package com.xavelo.template.render.api.adapter.in.http.admin;

import com.xavelo.template.render.domain.quote.Quote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger logger = LogManager.getLogger(AdminController.class);

    @PostMapping("/quote")
    public ResponseEntity<Quote> createQuote(@RequestBody QuoteRequest request) {
        Quote quote = request.toDomainQuote();
        logger.info("Received quote '{}' from author {}", quote.getText(), quote.getAuthor().getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(quote);
    }
}
