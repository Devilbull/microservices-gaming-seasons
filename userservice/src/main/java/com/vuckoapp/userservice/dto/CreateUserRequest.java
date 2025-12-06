// java
package com.vuckoapp.userservice.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateUserRequest(
    @NotBlank @Size(max = 50) String username,
    @NotBlank @Size(min = 8, max = 255) String password,
    @NotBlank @Size(max = 120) String fullName,
    @Email @NotBlank @Size(max = 120) String email,
    @NotNull LocalDate dateOfBirth,
    @NotBlank String role
) {}