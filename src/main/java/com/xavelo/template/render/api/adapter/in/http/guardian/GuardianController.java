package com.xavelo.template.render.api.adapter.in.http.guardian;

import com.xavelo.template.render.api.application.port.in.CreateGuardianUseCase;
import com.xavelo.template.render.api.application.port.in.ListGuardiansUseCase;
import com.xavelo.template.render.api.application.port.in.GetGuardianUseCase;
import com.xavelo.template.render.api.application.port.in.UpdateGuardianEmailUseCase;
import com.xavelo.template.render.api.adapter.in.http.guardian.UpdateGuardianEmailRequest;
import com.xavelo.template.render.api.domain.Guardian;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class GuardianController {

    private final CreateGuardianUseCase createGuardianUseCase;
    private final ListGuardiansUseCase listGuardiansUseCase;
    private final GetGuardianUseCase getGuardianUseCase;
    private final UpdateGuardianEmailUseCase updateGuardianEmailUseCase;

    public GuardianController(CreateGuardianUseCase createGuardianUseCase,
                              ListGuardiansUseCase listGuardiansUseCase,
                              GetGuardianUseCase getGuardianUseCase,
                              UpdateGuardianEmailUseCase updateGuardianEmailUseCase) {
        this.createGuardianUseCase = createGuardianUseCase;
        this.listGuardiansUseCase = listGuardiansUseCase;
        this.getGuardianUseCase = getGuardianUseCase;
        this.updateGuardianEmailUseCase = updateGuardianEmailUseCase;
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

    @GetMapping("/guardian/{id}")
    public ResponseEntity<Guardian> getGuardian(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return getGuardianUseCase.getGuardian(uuid)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/guardian/{id}/email")
    public ResponseEntity<Guardian> updateEmail(@PathVariable String id,
                                                @Valid @RequestBody UpdateGuardianEmailRequest request) {
        try {
            UUID uuid = UUID.fromString(id);
            return updateGuardianEmailUseCase.updateEmail(uuid, request.email())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
