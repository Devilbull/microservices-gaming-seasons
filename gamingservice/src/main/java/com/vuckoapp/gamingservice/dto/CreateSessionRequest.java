package com.vuckoapp.gamingservice.dto;

import com.vuckoapp.gamingservice.model.SessionType;

import java.time.LocalDateTime;

public record CreateSessionRequest(
        String sessionName,
        String gameName,
        int maxPlayers,
        SessionType sessionType,
        LocalDateTime startOfSession,
        String description
) {}
