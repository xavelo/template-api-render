package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.in.AssignGuardiansToStudentUseCase;
import com.xavelo.template.render.api.application.port.in.CreateStudentUseCase;
import com.xavelo.template.render.api.application.port.in.ListStudentsUseCase;
import com.xavelo.template.render.api.application.port.out.AssignGuardiansToStudentPort;
import com.xavelo.template.render.api.application.port.out.CreateStudentPort;
import com.xavelo.template.render.api.application.port.out.ListStudentsPort;
import com.xavelo.template.render.api.domain.Student;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService implements CreateStudentUseCase, ListStudentsUseCase, AssignGuardiansToStudentUseCase {

    private final CreateStudentPort createStudentPort;
    private final ListStudentsPort listStudentsPort;
    private final AssignGuardiansToStudentPort assignGuardiansToStudentPort;

    public StudentService(CreateStudentPort createStudentPort,
                          ListStudentsPort listStudentsPort,
                          AssignGuardiansToStudentPort assignGuardiansToStudentPort) {
        this.createStudentPort = createStudentPort;
        this.listStudentsPort = listStudentsPort;
        this.assignGuardiansToStudentPort = assignGuardiansToStudentPort;
    }

    @Override
    public Student createStudent(String name, List<UUID> guardianIds) {
        List<UUID> ids = guardianIds != null ? guardianIds : Collections.emptyList();
        Student student = new Student(UUID.randomUUID(), name, ids);
        return createStudentPort.createStudent(student);
    }

    @Override
    public List<Student> listStudents() {
        return listStudentsPort.listStudents();
    }

    @Override
    public void assignGuardians(UUID studentId, List<UUID> guardianIds) {
        List<UUID> ids = guardianIds != null ? guardianIds : Collections.emptyList();
        assignGuardiansToStudentPort.assignGuardiansToStudent(studentId, ids);
    }
}
