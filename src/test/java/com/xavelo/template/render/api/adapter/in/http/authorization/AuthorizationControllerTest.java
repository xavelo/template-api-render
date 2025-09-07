package com.xavelo.template.render.api.adapter.in.http.authorization;

import com.xavelo.template.render.api.application.port.in.AssignStudentsToAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.ListNotificationsByAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.CreateAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.GetAuthorizationUseCase;
import com.xavelo.template.render.api.application.port.in.ListAuthorizationsUseCase;
import com.xavelo.template.render.api.application.port.in.SendNotificationsUseCase;
import com.xavelo.template.render.api.domain.Notification;
import com.xavelo.template.render.api.domain.NotificationStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    private ListNotificationsByAuthorizationUseCase listNotificationsByAuthorizationUseCase;

    @MockBean
    private SendNotificationsUseCase sendNotificationsUseCase;

    @Test
    void whenListingNotifications_thenReturnsOk() throws Exception {
        UUID authorizationId = UUID.randomUUID();
        Notification notification = new Notification(UUID.randomUUID(), authorizationId,
                UUID.randomUUID(), UUID.randomUUID(), NotificationStatus.SENT, null, null, null);
        Mockito.when(listNotificationsByAuthorizationUseCase.listNotifications(authorizationId)).thenReturn(List.of(notification));

        mockMvc.perform(get("/api/authorization/" + authorizationId + "/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(notification.id().toString()));
    }

    @Test
    void whenSendingNotifications_thenReturnsOk() throws Exception {
        UUID authorizationId = UUID.randomUUID();
        mockMvc.perform(post("/api/authorization/" + authorizationId + "/notifications/send"))
                .andExpect(status().isOk());
        Mockito.verify(sendNotificationsUseCase).sendNotifications(authorizationId);
    }
}
