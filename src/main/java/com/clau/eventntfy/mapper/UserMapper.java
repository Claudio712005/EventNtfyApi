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

  @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "localDateTimeToString")
  @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "localDateTimeToString")
  UserResponseDTO toUserResponseDTO(User user);

  User toUser(UserRequestDTO userResponseDTO);

  @Named("localDateTimeToString")
  default String localDateTimeToString(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    return localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
  }
}
