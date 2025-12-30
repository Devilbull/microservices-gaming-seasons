package com.vuckoapp.gamingservice.dto;

import com.vuckoapp.gamingservice.model.SessionType;

public record SessionSearchDto(
        String gameName,
        SessionType sessionType,
        Integer maxNumPlayers,
        String keywords,
        Boolean isJoined,
        String sortBy,
        String order

) {
}
