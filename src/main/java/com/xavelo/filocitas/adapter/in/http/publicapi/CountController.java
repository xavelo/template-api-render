package com.xavelo.filocitas.adapter.in.http.publicapi;

import com.xavelo.filocitas.port.in.GetAuthorsCountUseCase;
import com.xavelo.filocitas.port.in.GetQuotesCountUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CountController {

    private static final Logger logger = LogManager.getLogger(CountController.class);

    private final GetAuthorsCountUseCase getAuthorsCountUseCase;
    private final GetQuotesCountUseCase getQuotesCountUseCase;

    public CountController(GetAuthorsCountUseCase getAuthorsCountUseCase,
                           GetQuotesCountUseCase getQuotesCountUseCase) {
        this.getAuthorsCountUseCase = getAuthorsCountUseCase;
        this.getQuotesCountUseCase = getQuotesCountUseCase;
    }

    @GetMapping("/authors/count")
    public ResponseEntity<CountResponse> getAuthorsCount() {
        logger.info("Fetching authors count");
        long authorsCount = getAuthorsCountUseCase.getAuthorsCount();
        return ResponseEntity.ok(new CountResponse(authorsCount));
    }

    @GetMapping("/quotes/count")
    public ResponseEntity<CountResponse> getQuotesCount() {
        logger.info("Fetching quotes count");
        long quotesCount = getQuotesCountUseCase.getQuotesCount();
        return ResponseEntity.ok(new CountResponse(quotesCount));
    }
}
