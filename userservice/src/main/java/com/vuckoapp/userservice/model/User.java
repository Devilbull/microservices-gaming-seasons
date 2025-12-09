package com.vuckoapp.userservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;



@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)   // <-- mapiranje na string/ENUM u bazi
    @Column(nullable = false, length = 20)
    private Role role;               // ADMIN ili GAMER

    @Enumerated(EnumType.STRING)   // <-- mapiranje na string/ENUM u bazi
    @Column(nullable = false, columnDefinition = "user_status")
    private UserStatus status;       // INITIALIZED, ACTIVE, BLOCKED



    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }
}
