package com.vuckoapp.userservice.dto;

import java.util.List;
import java.util.UUID;

public record SessionAttendanceDto(
        UUID organizerId,
        List<String> presentUsers, // emails
        List<String> absentUsers // emails
) {}