package com.clau.eventntfy.repository;

import com.clau.eventntfy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

  public boolean existsByEmail(String email);

  public boolean existsByUsername(String username);

  public boolean existsByPhoneNumber(String phoneNumber);

  public boolean existsByPhoneNumberOrEmailOrUsername(String phoneNumber, String email, String username);

  public boolean existsByIdNotAndPhoneNumberOrEmailOrUsername(Long id, String phoneNumber, String email, String username);

  public List<User> findByEmailIn(List<String> email);
}
