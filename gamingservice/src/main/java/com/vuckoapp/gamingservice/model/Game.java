package com.vuckoapp.gamingservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "game")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
    //ime, opis, žanr
    @Id
    @Column(name = "game_id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "game_name",nullable = false, length = 100)
    private String gameName;

    @Column(name = "description",nullable = false, length = 300)
    private String gameDescription;

    @Column(name = "game_type",nullable = false, length = 100)
    private String gameType;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
