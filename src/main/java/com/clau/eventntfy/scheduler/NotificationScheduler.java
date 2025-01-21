package com.clau.eventntfy.scheduler;

import com.clau.eventntfy.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

  private final NotificationService notificationService;

  private static final Logger LOGGER = LoggerFactory.getLogger(NotificationScheduler.class);

  @Scheduled(cron = "0 * * * * *")
  public void sendNotifications() {
    LOGGER.info("Verificando notificações agendadas para envio...");
    notificationService.sendNotifications();
  }
}
