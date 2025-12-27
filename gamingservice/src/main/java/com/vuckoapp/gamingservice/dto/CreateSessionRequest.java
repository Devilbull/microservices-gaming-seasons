package com.vuckoapp.gamingservice.dto;

import com.vuckoapp.gamingservice.model.SessionType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateSessionRequest(

        @NotBlank(message = "Session name is required")
        String sessionName,

        @NotBlank(message = "Game name is required")
        String gameName,

        @Min(value = 1, message = "At least 1 player is required")
        int maxPlayers,

        @NotNull(message = "Session type is required")
        SessionType sessionType,

        @NotNull(message = "Start of session is required")
        @FutureOrPresent(message = "Start time must be now or in the future")
        LocalDateTime startOfSession,

        @NotBlank(message = "Description is required")
        String description
) {}
