package com.xavelo.template.render.api.application.port.out;

import com.xavelo.template.render.api.domain.Student;

public interface CreateStudentPort {
    Student createStudent(Student student);
}
