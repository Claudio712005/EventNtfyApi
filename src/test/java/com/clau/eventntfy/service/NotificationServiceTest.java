package com.clau.eventntfy.service;

import com.clau.eventntfy.dto.request.NotificationRequestDTO;
import com.clau.eventntfy.enums.NotificationStatus;
import com.clau.eventntfy.enums.TypeNotification;
import com.clau.eventntfy.mapper.NotificationMapper;
import com.clau.eventntfy.model.Notification;
import com.clau.eventntfy.model.User;
import com.clau.eventntfy.repository.NotificationRepository;
import com.clau.eventntfy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

  @InjectMocks
  private NotificationService notificationService;

  @Mock
  private NotificationRepository notificationRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private NotificationMapper notificationMapper;

  @Mock
  private EmailService emailService;

  @Mock
  private SmsService smsService;

  private NotificationRequestDTO requestDTO;
  private Notification notification;
  private User sender;

  @BeforeEach
  void setUp() {
    requestDTO = new NotificationRequestDTO();
    requestDTO.setUserId(1L);
    requestDTO.setType(TypeNotification.EMAIL);
    requestDTO.setSubject("Test Subject");
    requestDTO.setEmailRecipient(List.of("recipient@example.com"));
    requestDTO.setScheduledTime(ZonedDateTime.now().plusHours(1).toLocalDateTime());

    sender = new User();
    sender.setId(1L);
    sender.setEmail("sender@example.com");

    notification = new Notification();
    notification.setId(1L);
    notification.setUser(sender);
    notification.setType(TypeNotification.EMAIL);
  }

  @Test
  @DisplayName("Deve salvar e enviar email ao salvar notificação")
  void deve_salvar_e_enviar_email_ao_salvar_notificacao() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
    when(userRepository.findByEmailIn(anyList())).thenReturn(List.of(sender));
    when(notificationMapper.toEntity(requestDTO)).thenReturn(notification);
    when(notificationRepository.save(notification)).thenReturn(notification);

    notificationService.saveNotification(requestDTO);

    verify(notificationRepository, times(1)).save(notification);
    verify(emailService, times(1)).sendEmail(notification);
    verify(smsService, never()).sendSms(notification);
  }

  @Test
  @DisplayName("Deve lançar exceção para data agendada inválida ao salvar notificação")
  void deve_lancar_excecao_para_data_agendada_invalida_ao_salvar_notificacao() {
    requestDTO.setScheduledTime(ZonedDateTime.now().minusHours(1).toLocalDateTime());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            notificationService.saveNotification(requestDTO));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals("Data agendada não pode ser anterior à data atual.", exception.getReason());
  }

  @Test
  @DisplayName("Deve deletar notificação com sucesso")
  void deve_deletar_notificacao_com_sucesso() {
    notification.setStatus(NotificationStatus.PENDING);
    when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

    notificationService.deleteNotification(1L);

    verify(notificationRepository, times(1)).delete(notification);
    verify(emailService, times(1)).sendEmail(notification);
    verify(smsService, never()).sendSms(notification);
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar deletar notificação já enviada")
  void deve_lancar_excecao_ao_tentar_deletar_notificacao_ja_enviada() {
    notification.setStatus(NotificationStatus.SENT);
    when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            notificationService.deleteNotification(1L));
    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    assertEquals("Não é possível cancelar uma notificação já enviada.", exception.getReason());
  }

  @Test
  @DisplayName("Deve processar notificações pendentes corretamente")
  void deve_processar_notificacoes_pendentes_corretamente() {
    notification.setStatus(NotificationStatus.PENDING);
    when(notificationRepository.findAllPendingNotifications(any(LocalDateTime.class))).thenReturn(List.of(notification));
    when(notificationRepository.save(notification)).thenReturn(notification);

    notificationService.sendNotifications();

    verify(notificationRepository, times(1)).save(notification);
    verify(emailService, times(1)).sendEmail(notification);
    verify(smsService, never()).sendSms(notification);
  }
}
