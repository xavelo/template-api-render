package com.xavelo.template.render.api.adapter.in.http.notification;

import com.xavelo.template.render.api.application.port.in.ListNotificationsUseCase;
import com.xavelo.template.render.api.application.port.in.ListNotificationsByAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.SendNotificationUseCase;
import com.xavelo.template.render.api.application.port.in.RespondNotificationUseCase;
import com.xavelo.template.render.api.domain.Notification;
import com.xavelo.template.render.api.domain.NotificationStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListNotificationsUseCase listNotificationsUseCase;

    @MockBean
    private ListNotificationsByAuthorizationUseCase listNotificationsByAuthorizationUseCase;

    @MockBean
    private SendNotificationUseCase sendNotificationUseCase;

    @MockBean
    private RespondNotificationUseCase respondNotificationUseCase;

    @Test
    void whenListingNotifications_thenReturnsOk() throws Exception {
        Notification notification = new Notification(UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), NotificationStatus.SENT, null, null, null, null);
        Mockito.when(listNotificationsUseCase.listNotifications()).thenReturn(List.of(notification));

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(notification.id().toString()));
    }

    @Test
    void whenListingNotificationsByAuthorization_thenReturnsOk() throws Exception {
        UUID authorizationId = UUID.randomUUID();
        Notification notification = new Notification(UUID.randomUUID(), authorizationId,
                UUID.randomUUID(), UUID.randomUUID(), NotificationStatus.SENT, null, null, null, null);
        Mockito.when(listNotificationsByAuthorizationUseCase.listNotifications(authorizationId)).thenReturn(List.of(notification));

        mockMvc.perform(get("/api/notifications/authorization/" + authorizationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(notification.id().toString()));
    }

    @Test
    void whenSendingNotification_thenReturnsOk() throws Exception {
        UUID notificationId = UUID.randomUUID();
        UUID sentBy = UUID.randomUUID();
        mockMvc.perform(post("/api/notification/" + notificationId + "/sent")
                .param("sentBy", sentBy.toString()))
                .andExpect(status().isOk());
        Mockito.verify(sendNotificationUseCase).sendNotification(notificationId, sentBy);
    }

    @Test
    void whenRespondingToNotification_thenReturnsOk() throws Exception {
        UUID notificationId = UUID.randomUUID();
        String json = "{\"status\":\"APPROVED\",\"respondedBy\":\"guardian\"}"; 
        mockMvc.perform(post("/api/notification/" + notificationId + "/response")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());
        Mockito.verify(respondNotificationUseCase).respondToNotification(notificationId, NotificationStatus.APPROVED, "guardian");
    }
}
