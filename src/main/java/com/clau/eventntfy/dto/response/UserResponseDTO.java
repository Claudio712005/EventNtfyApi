package com.clau.eventntfy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO extends RepresentationModel<UserResponseDTO> {

  private Long id;
  private String username;
  private String email;
  private String phoneNumber;
  private String createdAt;
  private String updatedAt;

  public UserResponseDTO(long l, String user1, String mail) {
  }
}
