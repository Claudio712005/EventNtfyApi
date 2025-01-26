package com.clau.eventntfy.service;

import com.clau.eventntfy.enums.NotificationStatus;
import com.clau.eventntfy.model.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@Service
@NoArgsConstructor
public class EmailService {

  private JavaMailSender javaMailSender;
  private ResourceLoader resourceLoader;

  @Value("classpath:templates/created-notification.html")
  private Resource createdNotificationTemplate;

  @Value("classpath:templates/alert-notification.html")
  private Resource alertNotificationTemplate;

  @Value("classpath:templates/deleted-notification.html")
  private Resource deletedNotificationTemplate;

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  @Autowired
  public EmailService(JavaMailSender javaMailSender, ResourceLoader resourceLoader) {
    this.javaMailSender = javaMailSender;
    this.resourceLoader = resourceLoader;
  }

  public void sendEmail(Notification notification) {
    try {
      String template = null;

      if (NotificationStatus.PENDING.equals(notification.getStatus())) {
        template = new String(createdNotificationTemplate.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
      } else if (NotificationStatus.SENT.equals(notification.getStatus())) {
        template = new String(alertNotificationTemplate.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
      } else if (NotificationStatus.FAILED.equals(notification.getStatus())) {
        template = new String(deletedNotificationTemplate.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
      }

      MimeMessage message = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setFrom(notification.getUser().getEmail());
      helper.setTo(notification.getRecipients().stream().map(user -> user.getEmail()).toArray(String[]::new));
      helper.setSubject(notification.getSubject());

      String content = template.replace("{{SUBJECT}}", notification.getSubject())
              .replace("{{MESSAGE}}", notification.getMessage())
              .replace("{{DATA_HORA}}", notification.getScheduledTime().format(dateTimeFormatter));

      helper.setText(content, true);

      javaMailSender.send(message);
    } catch (MessagingException e) {
      LOGGER.error("Erro ao enviar o email: ", e);
    } catch (IOException e) {
      LOGGER.error("Erro ao ler o arquivo de template: ", e);
    }
  }
}
