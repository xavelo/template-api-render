package com.xavelo.template.render.api.adapter.in.http.student;

import com.xavelo.template.render.api.application.port.in.AssignGuardiansToStudentUseCase;
import com.xavelo.template.render.api.application.port.in.CreateStudentUseCase;
import com.xavelo.template.render.api.application.port.in.ListStudentsUseCase;
import com.xavelo.template.render.api.domain.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final CreateStudentUseCase createStudentUseCase;
    private final ListStudentsUseCase listStudentsUseCase;
    private final AssignGuardiansToStudentUseCase assignGuardiansToStudentUseCase;

    public StudentController(CreateStudentUseCase createStudentUseCase,
                             ListStudentsUseCase listStudentsUseCase,
                             AssignGuardiansToStudentUseCase assignGuardiansToStudentUseCase) {
        this.createStudentUseCase = createStudentUseCase;
        this.listStudentsUseCase = listStudentsUseCase;
        this.assignGuardiansToStudentUseCase = assignGuardiansToStudentUseCase;
    }

    @PostMapping("/student")
    public ResponseEntity<Student> createStudent(@RequestBody CreateStudentRequest request) {
        Student saved = createStudentUseCase.createStudent(request.name(), request.guardianIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> listStudents() {
        List<Student> students = listStudentsUseCase.listStudents();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/student/{studentId}/guardians")
    public ResponseEntity<Void> assignGuardians(@PathVariable String studentId,
                                                @RequestBody AssignGuardiansRequest request) {
        try {
            if (request == null || request.guardianIds() == null || request.guardianIds().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            UUID studentUuid = UUID.fromString(studentId);
            List<UUID> guardianUuids = request.guardianIds().stream()
                    .map(UUID::fromString)
                    .toList();
            assignGuardiansToStudentUseCase.assignGuardians(studentUuid, guardianUuids);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
