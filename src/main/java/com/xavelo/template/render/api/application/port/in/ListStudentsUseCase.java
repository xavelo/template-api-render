package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Student;

import java.util.List;

public interface ListStudentsUseCase {
    List<Student> listStudents();
}

