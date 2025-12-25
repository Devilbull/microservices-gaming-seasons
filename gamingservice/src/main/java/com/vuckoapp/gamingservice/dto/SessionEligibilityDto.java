package com.vuckoapp.gamingservice.dto;

public record SessionEligibilityDto(
        boolean blocked,
        boolean attendanceOk
) {}
