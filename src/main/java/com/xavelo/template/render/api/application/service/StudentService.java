package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.CreateStudentUseCase;
import com.xavelo.template.render.api.application.port.out.CreateStudentPort;
import com.xavelo.template.render.api.domain.Student;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StudentService implements CreateStudentUseCase {

    private final CreateStudentPort createStudentPort;

    public StudentService(CreateStudentPort createStudentPort) {
        this.createStudentPort = createStudentPort;
    }

    @Override
    public Student createStudent(String name) {
        Student student = new Student(UUID.randomUUID(), name);
        return createStudentPort.createStudent(student);
    }
}
