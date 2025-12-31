package com.vuckoapp.gamingservice.dto;

import java.util.UUID;

public record InviteRequest(
        UUID sessionId,
        UUID invitedUserId
) {}
