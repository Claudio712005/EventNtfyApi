package com.clau.eventntfy.service;

import com.clau.eventntfy.config.TwilioConfig;
import com.clau.eventntfy.model.Notification;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class SmsService {

  private final TwilioConfig twilioConfig;
  private static final Logger LOGGER = LoggerFactory.getLogger(SmsService.class);

  public String createSmsTemplate(Notification notification) {
    String template = "";

    if(notification.getStatus() == null) {
      throw new IllegalStateException("Status desconhecido para o evento.");
    }

    switch (notification.getStatus()) {
      case PENDING:
        template = "Cadastro de Evento Confirmado\n\n"
                + "Evento: {{SUBJECT}}\n"
                + "Mensagem: {{MESSAGE}}\n"
                + "Data e Hora: {{DATA_HORA}}\n"
                + "Fique atento para mais informações.";
        break;

      case FAILED:
        template = "Evento Cancelado\n\n"
                + "O evento {{SUBJECT}} foi cancelado.\n"
                + "Mensagem: {{MESSAGE}}\n"
                + "Data e Hora: {{DATA_HORA}}\n"
                + "Se precisar de mais informações, entre em contato.";
        break;

      case SENT:
        template = "Aviso de Evento: Chegou a Hora!\n\n"
                + "O evento {{SUBJECT}} está prestes a acontecer agora.\n"
                + "Mensagem: {{MESSAGE}}\n"
                + "Data e Hora: {{DATA_HORA}}\n"
                + "Este é o momento de participação!";
        break;

    }

    String content = template.replace("{{SUBJECT}}", notification.getSubject())
            .replace("{{MESSAGE}}", notification.getMessage())
            .replace("{{DATA_HORA}}", notification.getScheduledTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

    return content;
  }

  public void sendSms(Notification notification) {
    try {
      String messageContent = createSmsTemplate(notification);

      notification.getRecipients().add(notification.getUser());

      notification.getRecipients().forEach(recipient -> {
        Message message = Message.creator(
                new PhoneNumber(recipient.getPhoneNumber()),
                new PhoneNumber(twilioConfig.getPhoneNumber()),
                messageContent
        ).create();
        LOGGER.info("Mensagem enviada com sucesso! ID: " + message.getSid());
      });

    } catch (Exception e) {
      LOGGER.error("Erro ao enviar mensagem: " + e.getMessage());
    }
  }
}
