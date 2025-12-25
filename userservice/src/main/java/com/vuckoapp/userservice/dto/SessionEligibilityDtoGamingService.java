package com.vuckoapp.userservice.dto;

public record SessionEligibilityDtoGamingService(
        boolean blocked,
        boolean attendanceOk
) {}
