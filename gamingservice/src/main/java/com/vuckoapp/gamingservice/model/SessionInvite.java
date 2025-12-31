package com.vuckoapp.gamingservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "session_invites")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class SessionInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID sessionId;
    private UUID invitedUserId;

    @Column(unique = true)
    private String token;

    private boolean used = false;
    private LocalDateTime expiryDate;
}