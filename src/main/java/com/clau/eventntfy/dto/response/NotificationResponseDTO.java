package com.clau.eventntfy.dto.response;

import com.clau.eventntfy.enums.TypeNotification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO extends RepresentationModel<NotificationResponseDTO> {

  private Long id;
  private UserResponseDTO user;
  private String message;
  private String createdAt;
  private String updatedAt;
  private String scheduledTime;
  private Integer priority;
  private TypeNotification type;
  private List<UserResponseDTO> emailRecipient;
  private String subject;

}
