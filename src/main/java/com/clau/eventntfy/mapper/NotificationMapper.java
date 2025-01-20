package com.clau.eventntfy.mapper;

import com.clau.eventntfy.dto.request.NotificationRequestDTO;
import com.clau.eventntfy.dto.response.NotificationResponseDTO;
import com.clau.eventntfy.dto.response.UserResponseDTO;
import com.clau.eventntfy.model.Notification;
import com.clau.eventntfy.model.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface NotificationMapper {

  NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "sent", constant = "false")
  @Mapping(target = "status", constant = "PENDING")
  Notification toEntity(NotificationRequestDTO requestDTO);

  @Mapping(target = "user", source = "user")
  @Mapping(target = "emailRecipient", source = "recipients", qualifiedByName = "mapRecipientsToResponse")
  @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd/MM/yyyy HH:mm:ss")
  @Mapping(target = "updatedAt", source = "updatedAt", dateFormat = "dd/MM/yyyy HH:mm:ss")
  @Mapping(target = "scheduledTime", source = "scheduledTime", dateFormat = "dd/MM/yyyy HH:mm:ss")
  NotificationResponseDTO toResponseDTO(Notification entity);

  @Named("mapRecipientsToResponse")
  default List<UserResponseDTO> mapRecipientsToResponse(List<User> recipients) {
    return recipients.stream()
            .map(user -> new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber(),
                    user.getCreatedAt() != null ? user.getCreatedAt().toString() : null,
                    user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null))
            .collect(Collectors.toList());
  }

}
