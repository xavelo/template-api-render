package com.xavelo.template.render.api.application.port.in;

import com.xavelo.template.render.api.domain.Student;

import java.util.List;
import java.util.UUID;

public interface CreateStudentUseCase {
    Student createStudent(String name, List<UUID> guardianIds);
}
