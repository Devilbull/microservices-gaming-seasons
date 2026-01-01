package com.vuckoapp.gamingservice.dto;

import java.util.List;
import java.util.UUID;

public record SessionLockDto(
        UUID sessionId,
        List<String> emails
) {
}
