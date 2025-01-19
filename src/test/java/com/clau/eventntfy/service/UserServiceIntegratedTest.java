package com.clau.eventntfy.service;

import com.clau.eventntfy.dto.request.UserRequestDTO;
import com.clau.eventntfy.dto.response.UserResponseDTO;
import com.clau.eventntfy.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = {"/sql/insert-users.sql"})
class UserServiceIntegratedTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Test
  @DirtiesContext
  @DisplayName("Deve retornar todos os usuários com sucesso")
  void deve_retornar_todos_os_usuarios_com_sucesso() {
    List<UserResponseDTO> users = userService.findAll();

    assertNotNull(users);
    assertEquals(2, users.size());
    assertEquals("Alice", users.get(0).getUsername());
    assertEquals("Bob", users.get(1).getUsername());
  }

  @Test
  @DirtiesContext
  @DisplayName("Deve salvar um novo usuário com sucesso")
  void deve_salvar_um_novo_usuario_com_sucesso() {
    UserRequestDTO newUser = new UserRequestDTO("Charlie", "charlie@example.com");
    userService.save(newUser);

    List<UserResponseDTO> users = userService.findAll();
    assertEquals(3, users.size());
    assertEquals("Charlie", users.get(2).getUsername());
  }

  @Test
  @DirtiesContext
  @DisplayName("Deve lançar exceção ao salvar usuário com email duplicado")
  void deve_lancar_excecao_ao_salvar_usuario_com_email_duplicado() {
    UserRequestDTO duplicateUser = new UserRequestDTO("Alice 2", "alice@example.com");

    Exception exception = assertThrows(Exception.class, () -> userService.save(duplicateUser));
    assertTrue(exception.getMessage().contains("Email já cadastrado"));
  }

  @Test
  @DirtiesContext
  @DisplayName("Deve excluir um usuário com sucesso")
  void deve_excluir_um_usuario_com_sucesso() {
    userService.deleteUser(1L);

    Exception exception = assertThrows(Exception.class, () -> userService.findById(1L));
    assertTrue(exception.getMessage().contains("Usuário não encontrado"));
  }

  @Test
  @DirtiesContext
  @DisplayName("Deve atualizar um usuário existente")
  void deve_atualizar_um_usuario_existente() {
    UserRequestDTO updatedUser = new UserRequestDTO("AliceUpdated", "alice_updated@example.com");
    userService.update(1L, updatedUser);

    UserResponseDTO response = userService.findById(1L);
    assertNotNull(response);
    assertEquals("AliceUpdated", response.getUsername());
    assertEquals("alice_updated@example.com", response.getEmail());
  }
}
