package com.clau.eventntfy.repository;

import com.clau.eventntfy.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  @Query("SELECT N FROM Notification N WHERE N.scheduledTime <= :currentTime AND N.status = 'PENDING'")
  List<Notification> findAllPendingNotifications(LocalDateTime currentTime);

}
