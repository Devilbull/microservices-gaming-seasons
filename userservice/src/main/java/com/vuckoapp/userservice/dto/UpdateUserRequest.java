package com.vuckoapp.userservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateUserRequest(



        @Size(max = 120)
        String fullName,

        LocalDate dateOfBirth

) {}

