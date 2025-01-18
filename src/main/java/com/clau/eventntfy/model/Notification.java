package com.clau.eventntfy.model;

import com.clau.eventntfy.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "message", nullable = false)
  private String message;

  @Column(name = "sent", nullable = false)
  private Boolean sent;

  @Column(name = "scheduled_time")
  private LocalDateTime scheduledTime;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationStatus status;

  @Column(name = "priority", nullable = false)
  private Integer priority;

  @Column(name = "type", nullable = false)
  private String type;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @ManyToMany
  @JoinTable(
          name = "notification_recipients",
          joinColumns = @JoinColumn(name = "notification_id"),
          inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private List<User> recipients = new ArrayList<>();
}
