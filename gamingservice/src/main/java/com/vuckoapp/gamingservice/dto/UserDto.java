// java
package com.vuckoapp.gamingservice.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String fullName,
        String email,
        LocalDate dateOfBirth,
        String role,
        String status,
        GamerStatsDto gamerStats

) {}
