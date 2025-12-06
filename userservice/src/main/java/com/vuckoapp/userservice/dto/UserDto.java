// java
package com.vuckoapp.userservice.dto;

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
        boolean isActivated
) {}
