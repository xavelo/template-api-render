package com.xavelo.template.render.api.adapter.in.http.notification;

import com.xavelo.template.render.api.application.port.in.NotificationUseCase;
import com.xavelo.template.render.api.domain.NotificationStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationUseCase notificationUseCase;

    @Test
    void whenMarkingNotificationSent_thenReturnsOk() throws Exception {
        UUID notificationId = UUID.randomUUID();
        mockMvc.perform(post("/api/notification/" + notificationId + "/sent"))
                .andExpect(status().isOk());
        Mockito.verify(notificationUseCase).markNotificationSent(notificationId);
    }

    @Test
    void whenRespondingToNotification_thenReturnsOk() throws Exception {
        UUID notificationId = UUID.randomUUID();
        String json = "{\"status\":\"APPROVED\",\"respondedBy\":\"guardian\"}";
        mockMvc.perform(post("/api/notification/" + notificationId + "/response")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
        Mockito.verify(notificationUseCase).respondToNotification(notificationId, NotificationStatus.APPROVED, "guardian");
    }
}
