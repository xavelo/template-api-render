package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.application.port.in.CreateStudentUseCase;
import com.xavelo.template.render.api.application.port.in.ListStudentsUseCase;
import com.xavelo.template.render.api.domain.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    private final CreateStudentUseCase createStudentUseCase;
    private final ListStudentsUseCase listStudentsUseCase;

    public StudentController(CreateStudentUseCase createStudentUseCase, ListStudentsUseCase listStudentsUseCase) {
        this.createStudentUseCase = createStudentUseCase;
        this.listStudentsUseCase = listStudentsUseCase;
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
}
