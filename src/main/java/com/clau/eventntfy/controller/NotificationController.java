package com.clau.eventntfy.controller;

import com.clau.eventntfy.dto.request.NotificationRequestDTO;
import com.clau.eventntfy.dto.response.NotificationResponseDTO;
import com.clau.eventntfy.mapper.NotificationMapper;
import com.clau.eventntfy.model.Notification;
import com.clau.eventntfy.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;
  private final NotificationMapper notificationMapper;

  @PostMapping
  public ResponseEntity createNotification(@RequestBody @Valid NotificationRequestDTO requestDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteNotification(@PathVariable Long id) {
    notificationService.deleteNotification(id);
    return ResponseEntity.ok("Notificação excluída com sucesso.");
  }

}
