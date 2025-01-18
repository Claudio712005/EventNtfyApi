package com.clau.eventntfy.repository;

import com.clau.eventntfy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  public boolean existsByEmail(String email);

  public boolean existsByUsername(String username);
}
