package com.xavelo.template.render.api.adapter.in.http.authorization;

import com.xavelo.template.render.api.application.port.in.AssignStudentsToAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.NotificationUseCase;
import com.xavelo.template.render.api.application.port.in.CreateAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.GetAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.ListAuthorizationsUseCase;
import com.xavelo.template.render.api.domain.Notification;
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

@WebMvcTest(AuthorizationController.class)
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateAuthorizationUseCase createAuthorizationUseCase;

    @MockBean
    private AssignStudentsToAuthorizationUseCase assignStudentsToAuthorizationUseCase;

    @MockBean
    private ListAuthorizationsUseCase listAuthorizationsUseCase;

    @MockBean
    private GetAuthorizationUseCase getAuthorizationUseCase;

    @MockBean
    private NotificationUseCase notificationUseCase;

    @Test
    void whenListingNotifications_thenReturnsOk() throws Exception {
        UUID authorizationId = UUID.randomUUID();
        Notification notification = new Notification(UUID.randomUUID(), authorizationId,
                UUID.randomUUID(), UUID.randomUUID(), "PENDING", null, null, null);
        Mockito.when(notificationUseCase.listNotifications(authorizationId)).thenReturn(List.of(notification));

        mockMvc.perform(get("/api/authorization/" + authorizationId + "/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(notification.id().toString()));
    }

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
        Mockito.verify(notificationUseCase).respondToNotification(notificationId, "APPROVED", "guardian");
    }
}
