package com.xavelo.template.render.api.application.service;

import com.xavelo.template.render.api.application.port.out.AssignGuardiansToStudentPort;
import com.xavelo.template.render.api.application.port.out.CreateStudentPort;
import com.xavelo.template.render.api.application.port.out.ListStudentsPort;
import com.xavelo.template.render.api.domain.Student;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class StudentServiceTest {

    private final CreateStudentPort createStudentPort = Mockito.mock(CreateStudentPort.class);
    private final ListStudentsPort listStudentsPort = Mockito.mock(ListStudentsPort.class);
    private final AssignGuardiansToStudentPort assignGuardiansToStudentPort = Mockito.mock(AssignGuardiansToStudentPort.class);

    private final StudentService studentService = new StudentService(createStudentPort, listStudentsPort, assignGuardiansToStudentPort);

    @Test
    void whenCreatingStudentWithoutGuardians_thenCreatesStudent() {
        Student student = new Student(UUID.randomUUID(), "John", List.of());
        Mockito.when(createStudentPort.createStudent(any(Student.class))).thenReturn(student);

        Student result = studentService.createStudent("John", List.of());
        assertEquals(student, result);
    }

    @Test
    void whenCreatingStudentWithGuardians_thenCreatesStudent() {
        UUID guardianId = UUID.randomUUID();
        Student student = new Student(UUID.randomUUID(), "John", List.of(guardianId));
        Mockito.when(createStudentPort.createStudent(any(Student.class))).thenReturn(student);

        Student result = studentService.createStudent("John", List.of(guardianId));
        assertEquals(student, result);
    }
}
