package com.clau.eventntfy.service;

import com.clau.eventntfy.dto.request.NotificationRequestDTO;
import com.clau.eventntfy.dto.response.NotificationResponseDTO;
import com.clau.eventntfy.enums.NotificationStatus;
import com.clau.eventntfy.enums.TypeNotification;
import com.clau.eventntfy.mapper.NotificationMapper;
import com.clau.eventntfy.model.Notification;
import com.clau.eventntfy.model.User;
import com.clau.eventntfy.repository.NotificationRepository;
import com.clau.eventntfy.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository repository;
  private final UserRepository userRepository;
  private final NotificationMapper mapper;
  private final EmailService emailService;
  private final SmsService smsService;

  private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  public void saveNotification(NotificationRequestDTO requestDTO) {
    if (requestDTO == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Notificação não informada.");
    }

    if (requestDTO.getScheduledTime().isBefore(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalDateTime())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data agendada não pode ser anterior à data atual.");
    }

    if (StringUtils.isBlank(requestDTO.getSubject()) && TypeNotification.EMAIL.equals(requestDTO.getType())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assunto do email não informado.");
    }

    User sender = userRepository.findById(requestDTO.getUserId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário remetente não encontrado."));

    List<User> recipients = userRepository.findByEmailIn(requestDTO.getEmailRecipient());
    if (recipients.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Destinatário(s) não encontrado(s).");
    }

    Notification notification = mapper.toEntity(requestDTO);
    notification.setUser(sender);
    notification.setRecipients(recipients);

    Notification notificationEntity = repository.save(notification);

    if (TypeNotification.EMAIL.equals(notificationEntity.getType())) {
      LOGGER.info("Enviando email de CRIAÇÃO para os destinatários...");
      emailService.sendEmail(notificationEntity);
    } else if (TypeNotification.SMS.equals(notificationEntity.getType())) {
      LOGGER.info("Enviando SMS de CRIAÇÃO para os destinatários...");
      smsService.sendSms(notification);
    }
  }

  public void deleteNotification(Long id) {
    if (id == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da notificação não informado.");
    }

    Notification notification = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada."));

    if(notification.getStatus() == NotificationStatus.SENT) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível cancelar uma notificação já enviada.");
    }

    if(notification.getStatus() == NotificationStatus.FAILED) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível cancelar uma notificação já cancelada.");
    }

    notification.setStatus(NotificationStatus.FAILED);
    notification.setMessage(("O Evento foi cancelado %s \n " +
            "%s").formatted(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).format(dateTimeFormatter), notification.getMessage()));

    if (TypeNotification.EMAIL.equals(notification.getType())) {
      LOGGER.info("Enviando email de cancelamento para os destinatários...");
      emailService.sendEmail(notification);
    } else if (TypeNotification.SMS.equals(notification.getType())) {
      LOGGER.info("Enviando SMS de cancelamento para os destinatários...");
      smsService.sendSms(notification);
    }

    notification.getRecipients().clear();
    repository.delete(notification);
  }

  public List<NotificationResponseDTO> findAllNotifications() {
    List<Notification> notifications = repository.findAll();
    return mapper.toCollectionResponseDTO(notifications).stream().toList();
  }

  public NotificationResponseDTO findById(Long id) {
    Notification notification = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada."));
    return mapper.toResponseDTO(notification);
  }

  public List<NotificationResponseDTO> findByUserId(Long userId) {
    List<Notification> notifications = repository.findByUser_Id(userId);
    return mapper.toCollectionResponseDTO(notifications).stream().toList();
  }

  public Page<NotificationResponseDTO> findAllNotificationsPaginated(int page, int size, String sort, String direction) {
    Sort sorting = Sort.by(Sort.Direction.fromString(direction), sort);
    PageRequest pageRequest = PageRequest.of(page, size, sorting);
    return repository.findAllNotifications(pageRequest)
            .map(mapper::toResponseDTO);
  }

  public void sendNotifications() {

    ZonedDateTime saoPauloTime = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
    LocalDateTime now = saoPauloTime.toLocalDateTime();

    List<Notification> notifications = repository.findAllPendingNotifications(now);

    notifications.forEach(notification -> {
      notification.setSent(true);
      notification.setStatus(NotificationStatus.SENT);

      if (TypeNotification.EMAIL.equals(notification.getType())) {
        LOGGER.info("Enviando email de agendamento para os destinatários...");
        emailService.sendEmail(notification);
      } else if (TypeNotification.SMS.equals(notification.getType())) {
        LOGGER.info("Enviando SMS de agendamento para os destinatários...");
        smsService.sendSms(notification);
      }

      repository.save(notification);
    });
  }

}
