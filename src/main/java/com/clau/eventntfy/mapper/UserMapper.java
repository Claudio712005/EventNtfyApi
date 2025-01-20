package com.clau.eventntfy.mapper;

import com.clau.eventntfy.dto.request.UserRequestDTO;
import com.clau.eventntfy.dto.response.UserResponseDTO;
import com.clau.eventntfy.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd/MM/yyyy HH:mm:ss")
  @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = "dd/MM/yyyy HH:mm:ss")
  UserResponseDTO toUserResponseDTO(User user);

  User toUser(UserRequestDTO userResponseDTO);
}
