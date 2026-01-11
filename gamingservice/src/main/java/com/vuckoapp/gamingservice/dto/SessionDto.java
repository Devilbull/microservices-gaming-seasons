package com.vuckoapp.gamingservice.dto;

import com.vuckoapp.gamingservice.model.SessionStatus;
import com.vuckoapp.gamingservice.model.SessionType;
import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionDto(
        UUID sessionId,
        UUID creatorId,
        String ownerUsername,
        String sessionName,
        String gameName,
        String description,
        SessionType sessionType,
        int maxPlayers,
        int currentPlayers,
        LocalDateTime startTime,
        boolean isUserJoined,
        SessionStatus sessionStatus
) {}