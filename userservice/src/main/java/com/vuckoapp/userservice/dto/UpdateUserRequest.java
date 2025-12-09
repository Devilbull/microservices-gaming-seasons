package com.vuckoapp.userservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateUserRequest(


        @Size(min = 8, max = 255)
        String password,

        @Size(max = 120)
        String fullName,

        @Email
        @Size(max = 120)
        String email,

        LocalDate dateOfBirth,

        String role

) {}

