package com.clau.eventntfy.service;

import com.clau.eventntfy.config.TwilioConfig;
import com.clau.eventntfy.enums.NotificationStatus;
import com.clau.eventntfy.model.Notification;
import com.clau.eventntfy.model.User;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SmsServiceTest {

  @Mock
  private TwilioConfig twilioConfig;

  @Mock
  private Logger logger;

  @InjectMocks
  private SmsService smsService;

  private Notification notification;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    when(twilioConfig.getPhoneNumber()).thenReturn("+1234567890");

    User user = new User();
    user.setPhoneNumber("+1987654321");
    user.setEmail("user@example.com");

    notification = new Notification();
    notification.setUser(user);
    notification.setRecipients(Collections.singletonList(user));
    notification.setMessage("Este é um teste de mensagem.");
    notification.setSubject("Teste de Evento");
    notification.setScheduledTime(LocalDateTime.of(2025, 1, 20, 12, 0));
  }

  @Test
  @DisplayName("Deve criar template de SMS para status PENDING com sucesso")
  void deve_criar_template_sms_para_status_pending_com_sucesso() {
    notification.setStatus(NotificationStatus.PENDING);

    String template = smsService.createSmsTemplate(notification);

    assertTrue(template.contains("Cadastro de Evento Confirmado"));
    assertTrue(template.contains("Evento: Teste de Evento"));
    assertTrue(template.contains("Mensagem: Este é um teste de mensagem."));
    assertTrue(template.contains("Data e Hora: 20/01/2025 12:00"));
  }

  @Test
  @DisplayName("Deve criar template de SMS para status FAILED com sucesso")
  void deve_criar_template_sms_para_status_failed_com_sucesso() {
    notification.setStatus(NotificationStatus.FAILED);

    String template = smsService.createSmsTemplate(notification);

    assertTrue(template.contains("Evento Cancelado"));
    assertTrue(template.contains("O evento Teste de Evento foi cancelado."));
    assertTrue(template.contains("Mensagem: Este é um teste de mensagem."));
    assertTrue(template.contains("Data e Hora: 20/01/2025 12:00"));
  }

  @Test
  @DisplayName("Deve criar template de SMS para status SENT com sucesso")
  void deve_criar_template_sms_para_status_sent_com_sucesso() {
    notification.setStatus(NotificationStatus.SENT);

    String template = smsService.createSmsTemplate(notification);

    assertTrue(template.contains("Aviso de Evento: Chegou a Hora!"));
    assertTrue(template.contains("O evento Teste de Evento está prestes a acontecer agora."));
    assertTrue(template.contains("Mensagem: Este é um teste de mensagem."));
    assertTrue(template.contains("Data e Hora: 20/01/2025 12:00"));
  }

  @Test
  @DisplayName("Deve lançar exceção ao criar template com status desconhecido")
  void deve_lancar_excecao_ao_criar_template_com_status_desconhecido() {
    notification.setStatus(null);

    IllegalStateException exception = assertThrows(IllegalStateException.class,
            () -> smsService.createSmsTemplate(notification));

    assertEquals("Status desconhecido para o evento.", exception.getMessage());
  }

}
