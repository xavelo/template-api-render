package com.xavelo.template.render.api.adapter.in.http.secure;

import com.xavelo.template.render.api.adapter.in.http.student.StudentController;
import com.xavelo.template.render.api.application.port.in.AssignGuardiansToStudentUseCase;
import com.xavelo.template.render.api.application.port.in.CreateStudentUseCase;
import com.xavelo.template.render.api.application.port.in.ListStudentsUseCase;
import com.xavelo.template.render.api.domain.Student;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateStudentUseCase createStudentUseCase;

    @MockBean
    private ListStudentsUseCase listStudentsUseCase;

    @MockBean
    private AssignGuardiansToStudentUseCase assignGuardiansToStudentUseCase;

    @Test
    void whenNoGuardianIds_thenReturnsCreated() throws Exception {
        Student student = new Student(UUID.randomUUID(), "John", List.of());
        Mockito.when(createStudentUseCase.createStudent(any(), anyList()))
                .thenReturn(student);

        String json = "{ \"name\": \"John\" }";
        mockMvc.perform(post("/api/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void whenValidRequestWithGuardianIds_thenReturnsCreated() throws Exception {
        UUID guardianId = UUID.randomUUID();
        Student student = new Student(UUID.randomUUID(), "John", List.of(guardianId));
        Mockito.when(createStudentUseCase.createStudent(any(), anyList()))
                .thenReturn(student);

        String json = "{ \"name\": \"John\", \"guardianIds\": [\"" + guardianId + "\"] }";
        mockMvc.perform(post("/api/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void whenListingStudents_thenReturnsOk() throws Exception {
        Student student = new Student(UUID.randomUUID(), "John", List.of());
       Mockito.when(listStudentsUseCase.listStudents()).thenReturn(List.of(student));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk());
    }

    @Test
    void whenAssigningGuardiansWithValidUuids_thenReturnsCreated() throws Exception {
        UUID studentId = UUID.randomUUID();
        String guardianId = UUID.randomUUID().toString();
        String json = "{ \"guardianIds\": [\"" + guardianId + "\"] }";

        mockMvc.perform(post("/api/student/" + studentId + "/guardians")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        Mockito.verify(assignGuardiansToStudentUseCase)
                .assignGuardians(Mockito.eq(studentId), Mockito.any());
    }

    @Test
    void whenAssigningGuardiansWithInvalidStudentUuid_thenReturnsBadRequest() throws Exception {
        String json = "{ \"guardianIds\": [\"" + UUID.randomUUID() + "\"] }";

        mockMvc.perform(post("/api/student/not-a-uuid/guardians")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAssigningGuardiansWithInvalidGuardianUuid_thenReturnsBadRequest() throws Exception {
        UUID studentId = UUID.randomUUID();
        String json = "{ \"guardianIds\": [\"invalid\"] }";

        mockMvc.perform(post("/api/student/" + studentId + "/guardians")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}
