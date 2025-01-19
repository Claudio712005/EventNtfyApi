package com.clau.eventntfy.repository;

import com.clau.eventntfy.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
