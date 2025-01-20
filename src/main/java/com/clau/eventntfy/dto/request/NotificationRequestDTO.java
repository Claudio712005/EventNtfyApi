package com.clau.eventntfy.dto.request;

import com.clau.eventntfy.enums.TypeNotification;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {

  @NotBlank(message = "Mensagem não informada")
  private String message;
  @NotNull(message = "Usuário não informado")
  private Long userId;
  @NotNull(message = "Data agendada não informada")
  private LocalDateTime scheduledTime;
  private Integer priority;
  @NotNull(message = "Tipo de notificação não informado")
  private TypeNotification type;
  @NotEmpty(message = "Destinatários do email não informado")
  private List<String> emailRecipient;
  private String subject;

}
