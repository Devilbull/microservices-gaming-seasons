package com.vuckoapp.gamingservice.services;

import com.vuckoapp.gamingservice.dto.SessionDto;
import com.vuckoapp.gamingservice.model.Session;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    @Mapping(target = "sessionId", source = "id")
    @Mapping(target = "gameName", source = "gameName")
    @Mapping(target = "startTime", source = "startOfSession")
    @Mapping(target = "currentPlayers", expression = "java(session.getParticipants() != null ? session.getParticipants().size() : 0)")
    @Mapping(target = "isUserJoined", ignore = true)
    SessionDto toDto(Session session);
}
