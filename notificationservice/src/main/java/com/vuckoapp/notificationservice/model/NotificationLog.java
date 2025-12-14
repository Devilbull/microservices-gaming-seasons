package com.vuckoapp.notificationservice.model;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "notification_log")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String subject;

    @Column(length = 2000)
    private String message;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

}

