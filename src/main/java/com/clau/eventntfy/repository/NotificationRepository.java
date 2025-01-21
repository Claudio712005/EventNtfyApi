package com.clau.eventntfy.repository;

import com.clau.eventntfy.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  @Query("SELECT N FROM Notification N WHERE N.scheduledTime <= :currentTime AND N.status = 'PENDING'")
  List<Notification> findAllPendingNotifications(LocalDateTime currentTime);

  List<Notification> findByUser_Id(Long userId);

  @Query("SELECT N FROM Notification N")
  Page<Notification> findAllNotifications(Pageable pageable);
}
