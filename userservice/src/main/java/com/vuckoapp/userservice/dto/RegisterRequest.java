package com.vuckoapp.userservice.dto;

import java.time.LocalDate;

public record RegisterRequest(
        String username,
        String password,
        String email,
        String fullName,
        LocalDate dateOfBirth
) {}
