package com.vuckoapp.gamingservice.dto;



import java.util.List;
import java.util.UUID;

public record SessionAttendanceDto(
        UUID organizerId,
        List<String> presentUsers, // emails
        List<String> absentUsers // emails
) {}
