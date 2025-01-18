package com.clau.eventntfy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

  @NotBlank(message = "Nome não informado")
  @NotNull(message = "Nome não informado")
  private String username;

  @NotBlank(message = "Email não informado")
  @NotNull(message = "Email não informado")
  private String email;

}
