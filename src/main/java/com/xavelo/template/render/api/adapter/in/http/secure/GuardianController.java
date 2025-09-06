package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.application.port.in.CreateGuardianUseCase;
import com.xavelo.template.render.api.application.port.in.ListGuardiansUseCase;
import com.xavelo.template.render.api.domain.Guardian;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GuardianController {

    private final CreateGuardianUseCase createGuardianUseCase;
    private final ListGuardiansUseCase listGuardiansUseCase;

    public GuardianController(CreateGuardianUseCase createGuardianUseCase, ListGuardiansUseCase listGuardiansUseCase) {
        this.createGuardianUseCase = createGuardianUseCase;
        this.listGuardiansUseCase = listGuardiansUseCase;
    }

    @GetMapping("/guardians")
    public ResponseEntity<List<Guardian>> listGuardians() {
        List<Guardian> guardians = listGuardiansUseCase.listGuardians();
        return ResponseEntity.ok(guardians);
    }

    @PostMapping("/guardian")
    public ResponseEntity<Guardian> createGuardian(@Valid @RequestBody CreateGuardianRequest request) {
        Guardian saved = createGuardianUseCase.createGuardian(request.name(), request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
