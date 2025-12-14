package com.vuckoapp.userservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
@Entity
@Table(name = "gamer_stats")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class GamerStats {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private int totalSessions = 0;

    @Column(nullable = false)
    private int numberOfJoinedSessions = 0;

    @Column(nullable = false)
    private int numberOfLeavingSessions = 0;

    @Column(nullable = false)
    private double attendanceNumber = 100.0;

    @Column(nullable = false)
    private int numberOfSuccessfulSessions = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrganizerTitle organizerTitle =  OrganizerTitle.BRONZE;

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }
}
