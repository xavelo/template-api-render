package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.application.port.in.CreateStudentUseCase;
import com.xavelo.template.render.api.domain.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final CreateStudentUseCase createStudentUseCase;

    public StudentController(CreateStudentUseCase createStudentUseCase) {
        this.createStudentUseCase = createStudentUseCase;
    }

    @PostMapping("/student")
    public ResponseEntity<Student> createStudent(@RequestBody CreateStudentRequest request) {
        Student saved = createStudentUseCase.createStudent(request.name(), request.guardianIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
