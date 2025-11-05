package com.xavelo.filocitas.adapter.in.http.ping;

import com.xavelo.filocitas.adapter.in.http.mapper.ApiMapper;
import com.xavelo.filocitas.api.HealthApi;
import com.xavelo.filocitas.api.model.PingResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PingController implements HealthApi {

    private static final Logger logger = LogManager.getLogger(PingController.class);

    private final ApiMapper apiMapper;

    @Value("${HOSTNAME:unknown}")
    private String podName;

    public PingController(ApiMapper apiMapper) {
        this.apiMapper = apiMapper;
    }

    @Override
    public ResponseEntity<PingResponse> ping() {
        var message = "pong from " + podName;
        logger.info("Responding to ping with message: {}", message);
        return ResponseEntity.ok(apiMapper.toPingResponse(message));
    }
}

