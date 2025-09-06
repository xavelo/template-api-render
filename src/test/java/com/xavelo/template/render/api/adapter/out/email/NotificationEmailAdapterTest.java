package com.xavelo.template.render.api.adapter.out.email;

import com.xavelo.template.render.api.application.port.out.NotificationEmailPort;
import com.xavelo.template.render.api.application.port.out.SendEmailPort;
import com.xavelo.template.render.api.application.port.out.GetGuardianPort;
import com.xavelo.template.render.api.domain.Guardian;
import com.xavelo.template.render.api.domain.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationEmailAdapterTest {

    @Mock
    private SendEmailPort sendEmailPort;

    @Mock
    private GetGuardianPort getGuardianPort;

    private NotificationEmailAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new NotificationEmailAdapter(sendEmailPort, getGuardianPort);
    }

    @Test
    void whenSendingNotification_thenDelegatesToSendEmailPort() {
        UUID guardianId = UUID.randomUUID();
        Notification notification = new Notification(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), guardianId, "PENDING", null, null, null);
        Guardian guardian = new Guardian(guardianId, "John", "john@example.com");
        Mockito.when(getGuardianPort.getGuardian(guardianId)).thenReturn(Optional.of(guardian));

        adapter.sendNotificationEmail(notification);

        Mockito.verify(sendEmailPort).sendEmail(Mockito.anyString(), Mockito.eq("john@example.com"), Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void whenSendEmailPortBeanPresent_thenBeanCreated() {
        ApplicationContextRunner runner = new ApplicationContextRunner()
                .withBean(SendEmailPort.class, () -> sendEmailPort)
                .withBean(GetGuardianPort.class, () -> getGuardianPort)
                .withUserConfiguration(NotificationEmailAdapter.class);

        runner.run(context -> assertThat(context).hasSingleBean(NotificationEmailAdapter.class));
    }

    @Test
    void whenAnotherNotificationEmailPortBean_thenAdapterNotCreated() {
        ApplicationContextRunner runner = new ApplicationContextRunner()
                .withBean(SendEmailPort.class, () -> sendEmailPort)
                .withBean(GetGuardianPort.class, () -> getGuardianPort)
                .withBean(NotificationEmailPort.class, () -> Mockito.mock(NotificationEmailPort.class))
                .withUserConfiguration(NotificationEmailAdapter.class);

        runner.run(context -> assertThat(context).doesNotHaveBean(NotificationEmailAdapter.class));
    }
}

