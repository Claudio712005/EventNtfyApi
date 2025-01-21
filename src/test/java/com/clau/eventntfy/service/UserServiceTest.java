package com.clau.eventntfy.service;

import com.clau.eventntfy.dto.request.UserRequestDTO;
import com.clau.eventntfy.dto.response.UserResponseDTO;
import com.clau.eventntfy.mapper.UserMapper;
import com.clau.eventntfy.model.User;
import com.clau.eventntfy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  private List<User> defaultUsers;

  @BeforeEach
  public void setUp() {

    MockitoAnnotations.openMocks(this);
    defaultUsers = List.of(
            new User(1L, "user1", "user1@gmail.com", "1111111111", LocalDateTime.now(), LocalDateTime.now(), null),
            new User(2L, "user2", "user2@gmail.com", "1111111111", LocalDateTime.now(), LocalDateTime.now(), null)
    );
  }

  @Test
  @DisplayName("Deve retornar todos os usuários com sucesso")
  void deve_retornar_todos_os_usuarios_com_sucesso() {
    when(userRepository.findAll()).thenReturn(defaultUsers);
    when(userMapper.toUserResponseDTO(any())).thenAnswer(invocation -> {
      User user = invocation.getArgument(0);
      return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    });

    List<UserResponseDTO> result = userService.findAll();

    assertEquals(2, result.size());
    verify(userRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("Deve retornar usuário por ID com sucesso")
  void deve_retornar_usuario_por_id_com_sucesso() {
    User user = defaultUsers.get(0);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userMapper.toUserResponseDTO(user)).thenReturn(new UserResponseDTO(1L, "user1", "user1@gmail.com"));

    UserResponseDTO result = userService.findById(1L);

    assertNotNull(result);
    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar encontrar usuário por ID inexistente")
  void deve_lancar_excecao_ao_tentar_encontrar_usuario_por_id_inexistente() {
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.findById(1L));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Usuário não encontrado", exception.getReason());

    verify(userRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Deve salvar usuário com sucesso")
  void deve_salvar_usuario_com_sucesso() {
    UserRequestDTO userRequestDTO = new UserRequestDTO("newUser", "newUser@gmail.com", "11111111");
    User user = new User(null, "newUser", "newUser@gmail.com", "11111111", null, null, null);
    User savedUser = new User(3L, "newUser", "newUser@gmail.com", "11111111", LocalDateTime.now(), LocalDateTime.now(), null);

    when(userMapper.toUser(userRequestDTO)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(savedUser);
    when(userMapper.toUserResponseDTO(savedUser)).thenReturn(new UserResponseDTO(3L, "newUser", "newUser@gmail.com"));

    userService.save(userRequestDTO);

    List<UserResponseDTO> result = userService.findAll();

    verify(userRepository, times(1)).save(user);
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar salvar usuário com dados inválidos")
  void deve_lancar_excecao_ao_tentar_salvar_usuario_com_dados_invalidos() {
    UserRequestDTO invalidUser = new UserRequestDTO(null, null, null);

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.save(invalidUser));

    assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    verify(userRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve excluir usuário com sucesso")
  void deve_excluir_usuario_com_sucesso() {
    when(userRepository.existsById(1L)).thenReturn(true);

    userService.deleteUser(1L);

    verify(userRepository, times(1)).deleteById(1L);
  }

  @Test
  @DisplayName("Deve lançar exceção ao tentar excluir usuário inexistente")
  void deve_lancar_excecao_ao_tentar_excluir_usuario_inexistente() {
    when(userRepository.existsById(1L)).thenReturn(false);

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.deleteUser(1L));

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    verify(userRepository, never()).deleteById(anyLong());
  }

  @Test
  @DisplayName("Deve atualizar usuário com sucesso")
  void deve_atualizar_usuario_com_sucesso() {
    Long id = 1L;
    UserRequestDTO userRequestDTO = new UserRequestDTO("updatedUser", "updatedUser@gmail.com", "11111111");
    User existingUser = defaultUsers.get(0);
    User updatedUser = new User(id, "updatedUser", "updatedUser@gmail.com", "11111111", existingUser.getCreatedAt(), LocalDateTime.now(), null);

    when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
    when(userMapper.toUser(userRequestDTO)).thenReturn(updatedUser);
    when(userRepository.save(updatedUser)).thenReturn(updatedUser);
    when(userMapper.toUserResponseDTO(updatedUser)).thenReturn(new UserResponseDTO(id, "updatedUser", "updatedUser@gmail.com"));

    userService.update(id, userRequestDTO);

    verify(userRepository, times(1)).save(updatedUser);
  }
}
