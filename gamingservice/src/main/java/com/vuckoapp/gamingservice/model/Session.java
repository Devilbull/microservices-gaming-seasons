package com.vuckoapp.gamingservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Session {

    @Id
    @Column(name = "session_id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @Column(nullable = false, length = 100, unique = true)
    private String sessionName;

    @Column(nullable = false, length = 100)
    private String gameName;

    @Column(nullable = false)
    private int maxPlayers;

    @Column(nullable = false)
    private int numberOfJoinedPlayers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionType sessionType;

    @Column(nullable = false)
    private LocalDateTime startOfSession;

    @Column(nullable = false, length = 300)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus sessionStatus;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
