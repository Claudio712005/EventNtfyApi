package com.clau.eventntfy.service;

import com.clau.eventntfy.model.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender javaMailSender;

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

  public void sendEmail(Notification notification) {
    try {
      MimeMessage message = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setFrom(notification.getUser().getEmail());
      helper.setTo(notification.getRecipients().stream().map(user -> user.getEmail()).toArray(String[]::new));
      helper.setSubject("Subject test");
      helper.setText(notification.getMessage());
    } catch (MessagingException e) {
      LOGGER.error("Erro ao enviar o email: ", e);
    }
  }
}
