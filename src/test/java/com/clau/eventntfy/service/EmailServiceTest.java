package com.clau.eventntfy.service;

import com.clau.eventntfy.enums.NotificationStatus;
import com.clau.eventntfy.model.Notification;
import com.clau.eventntfy.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

  @Mock
  private JavaMailSender javaMailSender;

  private Resource createdNotificationTemplate;
  private Resource alertNotificationTemplate;
  private Resource deletedNotificationTemplate;

  private EmailService emailService;

  private Notification notification;

  @BeforeEach
  void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);

    createdNotificationTemplate = mock(Resource.class);
    alertNotificationTemplate = mock(Resource.class);
    deletedNotificationTemplate = mock(Resource.class);

    when(createdNotificationTemplate.getInputStream()).thenReturn(
            new ByteArrayInputStream("<html>PENDING {{SUBJECT}}</html>".getBytes(StandardCharsets.UTF_8))
    );
    when(alertNotificationTemplate.getInputStream()).thenReturn(
            new ByteArrayInputStream("<html>SENT {{SUBJECT}}</html>".getBytes(StandardCharsets.UTF_8))
    );
    when(deletedNotificationTemplate.getInputStream()).thenReturn(
            new ByteArrayInputStream("<html>FAILED {{SUBJECT}}</html>".getBytes(StandardCharsets.UTF_8))
    );

    emailService = new EmailService(
            javaMailSender,
            null,
            createdNotificationTemplate,
            alertNotificationTemplate,
            deletedNotificationTemplate
    );

    // Configuração da notificação
    User user = new User();
    user.setEmail("user@example.com");

    notification = new Notification();
    notification.setUser(user);
    notification.setRecipients(Collections.singletonList(user));
    notification.setMessage("Este é um teste de mensagem.");
    notification.setSubject("Teste de Evento");
    notification.setScheduledTime(LocalDateTime.of(2025, 1, 20, 12, 0));
  }


  @Test
  @DisplayName("Deve enviar email com template PENDING")
  void deveEnviarEmailComTemplatePending() throws MessagingException {
    notification.setStatus(NotificationStatus.PENDING);

    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

    emailService.sendEmail(notification);

    verify(javaMailSender, times(1)).send(mimeMessage);
  }

  @Test
  @DisplayName("Deve enviar email com template SENT")
  void deveEnviarEmailComTemplateSent() throws MessagingException {
    notification.setStatus(NotificationStatus.SENT);

    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

    emailService.sendEmail(notification);

    verify(javaMailSender, times(1)).send(mimeMessage);
  }

  @Test
  @DisplayName("Deve enviar email com template FAILED")
  void deveEnviarEmailComTemplateFailed() throws MessagingException {
    notification.setStatus(NotificationStatus.FAILED);

    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

    emailService.sendEmail(notification);

    verify(javaMailSender, times(1)).send(mimeMessage);
  }

  @Test
  @DisplayName("Deve lançar exceção ao enviar o email")
  void deveLancarExcecaoAoEnviarEmail() {
    notification.setStatus(NotificationStatus.PENDING);

    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

    // Use uma RuntimeException ao simular um erro
    doThrow(new RuntimeException("Erro ao enviar o email")).when(javaMailSender).send(mimeMessage);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> emailService.sendEmail(notification));
    assertEquals("Erro ao enviar o email", exception.getMessage());
  }

}
