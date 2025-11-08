package com.xavelo.filocitas.adapter.in.http.metrics;

import com.xavelo.filocitas.adapter.in.http.mapper.ApiMapper;
import com.xavelo.filocitas.api.MetricsApi;
import com.xavelo.filocitas.api.model.CountResponse;
import com.xavelo.filocitas.port.in.GetAuthorsCountUseCase;
import com.xavelo.filocitas.port.in.GetQuotesCountUseCase;
import com.xavelo.filocitas.port.in.GetQuotesLikesCountUseCase;
import com.xavelo.filocitas.port.in.GetTagsCountUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MetricsController implements MetricsApi {

    private static final Logger logger = LogManager.getLogger(MetricsController.class);

    private final GetAuthorsCountUseCase getAuthorsCountUseCase;
    private final GetQuotesCountUseCase getQuotesCountUseCase;
    private final GetQuotesLikesCountUseCase getQuotesLikesCountUseCase;
    private final GetTagsCountUseCase getTagsCountUseCase;
    private final ApiMapper apiMapper;

    public MetricsController(GetAuthorsCountUseCase getAuthorsCountUseCase,
                             GetQuotesCountUseCase getQuotesCountUseCase,
                             GetQuotesLikesCountUseCase getQuotesLikesCountUseCase,
                             GetTagsCountUseCase getTagsCountUseCase,
                             ApiMapper apiMapper) {
        this.getAuthorsCountUseCase = getAuthorsCountUseCase;
        this.getQuotesCountUseCase = getQuotesCountUseCase;
        this.getQuotesLikesCountUseCase = getQuotesLikesCountUseCase;
        this.getTagsCountUseCase = getTagsCountUseCase;
        this.apiMapper = apiMapper;
    }

    @Override
    public ResponseEntity<CountResponse> getAuthorsCount() {
        logger.info("Fetching authors count");
        long authorsCount = getAuthorsCountUseCase.getAuthorsCount();
        return ResponseEntity.ok(apiMapper.toCountResponse(authorsCount));
    }

    @Override
    public ResponseEntity<CountResponse> getQuotesCount() {
        logger.info("Fetching quotes count");
        long quotesCount = getQuotesCountUseCase.getQuotesCount();
        return ResponseEntity.ok(apiMapper.toCountResponse(quotesCount));
    }

    @Override
    public ResponseEntity<CountResponse> getTagsCount() {
        logger.info("Fetching tags count");
        long tagsCount = getTagsCountUseCase.getTagsCount();
        return ResponseEntity.ok(apiMapper.toCountResponse(tagsCount));
    }

    @Override
    public ResponseEntity<CountResponse> getLikesCount() {
        logger.info("Fetching total quote likes");
        long likesCount = getQuotesLikesCountUseCase.getQuotesLikesCount();
        return ResponseEntity.ok(apiMapper.toCountResponse(likesCount));
    }
}
