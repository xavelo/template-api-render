package com.xavelo.template.render.api.adapter.out.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PostgresAdapterIntegrationTest {

    @Autowired
    private PostgresAdapter postgresAdapter;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GuardianRepository guardianRepository;

    @Test
    void assignGuardiansToStudent_persistsGuardians() {
        Guardian guardian = new Guardian();
        guardian.setName("Guardian");
        guardian.setEmail("g@example.com");
        guardian = guardianRepository.save(guardian);

        Student student = new Student();
        student.setName("Student");
        student = studentRepository.save(student);

        postgresAdapter.assignGuardiansToStudent(student.getId(), List.of(guardian.getId()));

        Student finalStudent = student;
        Student updated = studentRepository.findAll()
                .stream()
                .filter(s -> s.getId().equals(finalStudent.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(updated.getGuardians()).hasSize(1);
        assertThat(updated.getGuardians().iterator().next().getId())
                .isEqualTo(guardian.getId());
    }
}
