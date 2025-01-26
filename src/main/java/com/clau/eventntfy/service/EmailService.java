package com.clau.eventntfy.service;

import com.clau.eventntfy.enums.NotificationStatus;
import com.clau.eventntfy.model.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class EmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private ResourceLoader resourceLoader;

  @Value("classpath:templates/created-notification.html")
  private Resource createdNotificationTemplate;

  @Value("classpath:templates/alert-notification.html")
  private Resource alertNotificationTemplate;

  @Value("classpath:templates/deleted-notification.html")
  private Resource deletedNotificationTemplate;

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  private String loadTemplate(Resource templateResource) throws IOException {
    return new String(templateResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
  }

  public void sendEmail(Notification notification) {
    try {
      String template = getTemplateByStatus(notification.getStatus());
      if (template == null) {
        LOGGER.error("Template não encontrado para o status: {}", notification.getStatus());
        return;
      }

      String content = template.replace("{{SUBJECT}}", notification.getSubject())
              .replace("{{MESSAGE}}", notification.getMessage())
              .replace("{{DATA_HORA}}", notification.getScheduledTime().format(dateTimeFormatter));

      MimeMessage message = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setFrom(notification.getUser().getEmail());
      helper.setTo(notification.getRecipients().stream().map(user -> user.getEmail()).toArray(String[]::new));
      helper.setSubject(notification.getSubject());
      helper.setText(content, true);

      javaMailSender.send(message);
    } catch (MessagingException e) {
      LOGGER.error("Erro ao enviar o email: ", e);
    } catch (IOException e) {
      LOGGER.error("Erro ao ler o arquivo de template: ", e);
    }
  }

  private String getTemplateByStatus(NotificationStatus status) throws IOException {
    switch (status) {
      case PENDING:
        return loadTemplate(createdNotificationTemplate);
      case SENT:
        return loadTemplate(alertNotificationTemplate);
      case FAILED:
        return loadTemplate(deletedNotificationTemplate);
      default:
        return null;
    }
  }
}
