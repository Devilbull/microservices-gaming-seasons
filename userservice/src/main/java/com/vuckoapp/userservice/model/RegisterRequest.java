package com.vuckoapp.userservice.model;

import java.time.LocalDate;

public record RegisterRequest(
        String username,
        String password,
        String email,
        String fullName,
        LocalDate dateOfBirth
) {}
