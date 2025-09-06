package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Student;

public interface CreateStudentUseCase {
    Student createStudent(String name);
}
