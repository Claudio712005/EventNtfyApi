package com.clau.eventntfy.service;

import com.clau.eventntfy.dto.request.NotificationRequestDTO;
import com.clau.eventntfy.mapper.NotificationMapper;
import com.clau.eventntfy.model.Notification;
import com.clau.eventntfy.model.User;
import com.clau.eventntfy.repository.NotificationRepository;
import com.clau.eventntfy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository repository;
  private final UserRepository userRepository;
  private final NotificationMapper mapper;
  private final EmailService emailService;

  public void saveNotification(NotificationRequestDTO requestDTO) {
    if (requestDTO == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Notificação não informada.");
    }

    if (requestDTO.getScheduledTime().isBefore(LocalDateTime.now())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data agendada não pode ser anterior à data atual.");
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

    emailService.sendEmail(notificationEntity);

    // TODO: Implementar envio de e-mail informando a criação da notificação.
  }

  public void deleteNotification(Long id) {
    if (id == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da notificação não informado.");
    }

    Notification notification = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada."));

    notification.getRecipients().clear();
    repository.delete(notification);

    // TODO: Implementar envio de e-mail informando a exclusão da notificação.
  }
}
