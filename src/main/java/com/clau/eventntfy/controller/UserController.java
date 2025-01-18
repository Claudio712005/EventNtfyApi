package com.clau.eventntfy.controller;

import com.clau.eventntfy.dto.request.UserRequestDTO;
import com.clau.eventntfy.dto.response.UserResponseDTO;
import com.clau.eventntfy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService service;

  @GetMapping()
  public List<UserResponseDTO> findAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public UserResponseDTO findById(@PathVariable Long id) {
    return service.findById(id);
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable Long id) {
    service.deleteUser(id);
  }

  @PostMapping()
  public void save(@RequestBody @Valid UserRequestDTO user) {
    service.save(user);
  }

  @PutMapping("/{id}")
  public void update(@PathVariable Long id, @RequestBody @Valid UserRequestDTO user) {
    service.update(id, user);
  }
}
