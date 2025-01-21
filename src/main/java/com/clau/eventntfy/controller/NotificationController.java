package com.clau.eventntfy.controller;

import com.clau.eventntfy.dto.request.NotificationRequestDTO;
import com.clau.eventntfy.dto.response.NotificationResponseDTO;
import com.clau.eventntfy.mapper.NotificationMapper;
import com.clau.eventntfy.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;
  private final NotificationMapper notificationMapper;

  @PostMapping
  public ResponseEntity createNotification(@RequestBody @Valid NotificationRequestDTO requestDTO) {
    notificationService.saveNotification(requestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteNotification(@PathVariable Long id) {
    notificationService.deleteNotification(id);
    return ResponseEntity.ok("Notificação excluída com sucesso.");
  }

  @GetMapping("/{id}")
  public ResponseEntity findById(@PathVariable Long id) {
    NotificationResponseDTO notification = notificationService.findById(id);

    notification.add(linkTo(methodOn(NotificationController.class).findById(notification.getId())).withSelfRel());
    notification.add(linkTo(methodOn(NotificationController.class).findAll()).withRel("all-notifications"));
    addUsersLinks(notification);

    return ResponseEntity.ok(notification);
  }

  @GetMapping
  public ResponseEntity findAll() {
    List<NotificationResponseDTO> notifications = notificationService.findAllNotifications();

    notifications.forEach(notification -> {
      notification.add(linkTo(methodOn(NotificationController.class).findById(notification.getId())).withSelfRel());
      notification.add(linkTo(methodOn(NotificationController.class).findAll()).withRel("all-notifications"));
      addUsersLinks(notification);
    });

    return ResponseEntity.ok(notifications);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity findByUserId(@PathVariable Long userId) {
    List<NotificationResponseDTO> notifications = notificationService.findByUserId(userId);

    notifications.forEach(notification -> {
      notification.add(linkTo(methodOn(NotificationController.class).findById(notification.getId())).withSelfRel());
      notification.add(linkTo(methodOn(NotificationController.class).findAll()).withRel("all-notifications"));
      addUsersLinks(notification);
    });

    return ResponseEntity.ok(notifications);
  }

  @GetMapping("/page")
  public ResponseEntity findAllPaginated(@RequestParam int page,
                                         @RequestParam int size,
                                         @RequestParam String sort,
                                         @RequestParam String direction) {
    List<NotificationResponseDTO> notifications = notificationService.findAllNotificationsPaginated(page, size, sort, direction).getContent();

    notifications.forEach(notification -> {
      notification.add(linkTo(methodOn(NotificationController.class).findById(notification.getId())).withSelfRel());
      notification.add(linkTo(methodOn(NotificationController.class).findAll()).withRel("all-notifications"));
      addUsersLinks(notification);
    });

    return ResponseEntity.ok(notifications);
  }

  private void addUsersLinks(NotificationResponseDTO notification) {
    notification.getUser().add(linkTo(methodOn(UserController.class).findById(notification.getUser().getId())).withSelfRel());
    notification.getEmailRecipient().forEach(user -> {
      user.add(linkTo(methodOn(UserController.class).findById(user.getId())).withSelfRel());
      user.add(linkTo(methodOn(UserController.class).findAll()).withRel("all-users"));
    });
  }

}
