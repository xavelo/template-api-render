package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.application.port.in.CreateAuthorizationUseCase;
import com.xavelo.template.render.api.domain.Authorization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthorizationController {

    private static final Logger logger = LogManager.getLogger(AuthorizationController.class);

    private final CreateAuthorizationUseCase createAuthorizationUseCase;

    public AuthorizationController(CreateAuthorizationUseCase createAuthorizationUseCase) {
        this.createAuthorizationUseCase = createAuthorizationUseCase;
    }

    @PostMapping("/authorization")
    public ResponseEntity<Authorization> createAuthorization(@RequestBody CreateAuthorizationRequest request) {
        Authorization authorization = createAuthorizationUseCase.createAuthorization(
                request.title(),
                request.text(),
                request.status(),
                request.createdBy(),
                request.sentBy(),
                request.approvedBy()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(authorization);
    }
}
