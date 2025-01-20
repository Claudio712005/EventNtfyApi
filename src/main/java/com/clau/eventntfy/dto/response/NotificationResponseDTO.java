package com.clau.eventntfy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {

  private UserResponseDTO user;
  private String message;
  private String createdAt;
  private String updatedAt;
  private String scheduledTime;
  private Integer priority;
  private String type;
  private List<UserResponseDTO> emailRecipient;

}
