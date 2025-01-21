package com.clau.eventntfy.service;

import com.clau.eventntfy.dto.request.UserRequestDTO;
import com.clau.eventntfy.dto.response.UserResponseDTO;
import com.clau.eventntfy.mapper.UserMapper;
import com.clau.eventntfy.model.User;
import com.clau.eventntfy.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final UserMapper userMapper;

  public void deleteUser(Long id) {
    if (id == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id não informado");
    }

    if (!repository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
    }

    repository.deleteById(id);
  }

  public List<UserResponseDTO> findAll() {
    return repository.findAll().stream().map(u -> userMapper.toUserResponseDTO(u)).toList();
  }

  public UserResponseDTO findById(Long id) {
    if (id == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id não informado");
    }

    return userMapper.toUserResponseDTO(
            repository.findById(id).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"))
    );
  }

  public void save(UserRequestDTO user) {
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não informado");
    }

    validarUnicidade(user);

    repository.save(userMapper.toUser(user));
  }

  public void update(Long id, UserRequestDTO user) {

    if(id == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id não informado");
    }

    if (user == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não informado");
    }

    validarUnicidade(user);

    final User existingUser = repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

    User updatedUser = userMapper.toUser(user);
    updatedUser.setId(id);
    updatedUser.setCreatedAt(existingUser.getCreatedAt());

    repository.save(updatedUser);
  }

  private void validarUnicidade(UserRequestDTO requestDTO){
    if(requestDTO == null){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não informado");
    }
    if(StringUtils.isBlank(requestDTO.getUsername())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome não informado");
    }
    if(StringUtils.isBlank(requestDTO.getEmail())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email não informado");
    }
    if(StringUtils.isBlank(requestDTO.getPhoneNumber())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone não informado");
    }
    if(repository.existsByUsername(requestDTO.getUsername())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome de usuário já cadastrado");
    }
    if(repository.existsByEmail(requestDTO.getEmail())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
    }
    if(repository.existsByPhoneNumber(requestDTO.getPhoneNumber())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefone já cadastrado");
    }

  }
}
