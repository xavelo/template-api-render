package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.application.port.in.CreateGuardianUseCase;
import com.xavelo.template.render.api.domain.Guardian;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GuardianController {

    private final CreateGuardianUseCase createGuardianUseCase;

    public GuardianController(CreateGuardianUseCase createGuardianUseCase) {
        this.createGuardianUseCase = createGuardianUseCase;
    }

    @PostMapping("/guardian")
    public ResponseEntity<Guardian> createGuardian(@RequestBody CreateGuardianRequest request) {
        Guardian saved = createGuardianUseCase.createGuardian(request.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
