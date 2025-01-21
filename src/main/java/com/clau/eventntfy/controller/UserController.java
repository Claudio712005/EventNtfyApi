package com.clau.eventntfy.controller;

import com.clau.eventntfy.dto.request.UserRequestDTO;
import com.clau.eventntfy.dto.response.UserResponseDTO;
import com.clau.eventntfy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService service;

  @GetMapping
  public CollectionModel<UserResponseDTO> findAll() {
    List<UserResponseDTO> users = service.findAll();

    users.forEach(user -> {
      user.add(linkTo(methodOn(UserController.class).findById(user.getId())).withSelfRel());
      user.add(linkTo(methodOn(UserController.class).findAll()).withRel("all-users"));
    });

    Link selfLink = linkTo(methodOn(UserController.class).findAll()).withSelfRel();
    return CollectionModel.of(users, selfLink);
  }

  @GetMapping("/{id}")
  public UserResponseDTO findById(@PathVariable Long id) {
    UserResponseDTO user = service.findById(id);

    user.add(linkTo(methodOn(UserController.class).findById(id)).withSelfRel());
    user.add(linkTo(methodOn(UserController.class).findAll()).withRel("all-users"));

    return user;
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
