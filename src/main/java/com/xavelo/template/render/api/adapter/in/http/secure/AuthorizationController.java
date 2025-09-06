package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.application.port.in.AssignStudentsToAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.CreateAuthorizationUseCase;
import com.xavelo.template.render.api.domain.Authorization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthorizationController {

    private static final Logger logger = LogManager.getLogger(AuthorizationController.class);

    private final CreateAuthorizationUseCase createAuthorizationUseCase;
    private final AssignStudentsToAuthorizationUseCase assignStudentsToAuthorizationUseCase;

    public AuthorizationController(CreateAuthorizationUseCase createAuthorizationUseCase,
                                   AssignStudentsToAuthorizationUseCase assignStudentsToAuthorizationUseCase) {
        this.createAuthorizationUseCase = createAuthorizationUseCase;
        this.assignStudentsToAuthorizationUseCase = assignStudentsToAuthorizationUseCase;
    }

    @PostMapping("/authorization")
    public ResponseEntity<Authorization> createAuthorization(@RequestBody CreateAuthorizationRequest request) {
        if (!StringUtils.hasText(request.title())
                || !StringUtils.hasText(request.text())
                || !StringUtils.hasText(request.status())
                || !StringUtils.hasText(request.createdBy())) {
            logger.warn("Missing required field when creating authorization");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

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

    @PostMapping("/authorization/{authorizationId}/students")
    public ResponseEntity<Void> assignStudentsToAuthorization(@PathVariable UUID authorizationId,
                                                              @RequestBody AssignStudentsRequest request) {
        assignStudentsToAuthorizationUseCase.assignStudents(authorizationId, request.studentIds());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
