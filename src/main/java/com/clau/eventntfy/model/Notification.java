package com.clau.eventntfy.model;

import com.clau.eventntfy.enums.NotificationStatus;
import com.clau.eventntfy.enums.TypeNotification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
  @Enumerated(EnumType.STRING)
  private TypeNotification type;

  @Column(name = "subject")
  private String subject;

  @Column(name = "created_at", updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "notification_recipients",
          joinColumns = @JoinColumn(name = "notification_id"),
          inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private List<User> recipients = new ArrayList<>();
}
