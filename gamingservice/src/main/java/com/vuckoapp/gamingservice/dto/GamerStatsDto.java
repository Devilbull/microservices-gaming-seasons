package com.vuckoapp.gamingservice.dto;

import com.vuckoapp.gamingservice.model.OrganizerTitle;

import java.math.BigDecimal;

public record GamerStatsDto(
        int totalSessions,
        int numberOfJoinedSessions,
        int numberOfLeavingSessions,
        BigDecimal attendanceNumber,
        int numberOfSuccessfulSessions,
        OrganizerTitle organizerTitle
) {
}
