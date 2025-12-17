package com.vuckoapp.userservice.dto;

import com.vuckoapp.userservice.model.OrganizerTitle;

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
