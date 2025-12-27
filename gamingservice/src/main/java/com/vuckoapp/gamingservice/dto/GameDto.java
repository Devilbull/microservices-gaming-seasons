package com.vuckoapp.gamingservice.dto;

import java.util.UUID;

public record GameDto(
        String gameName,
        String gameDescription,
        String gameType,

        UUID id
){
}
