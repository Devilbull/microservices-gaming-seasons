package com.vuckoapp.userservice.services.mapper;

import com.vuckoapp.userservice.dto.GamerStatsDto;
import com.vuckoapp.userservice.dto.UserDto;
import com.vuckoapp.userservice.model.GamerStats;
import com.vuckoapp.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "status", expression = "java(user.getStatus().name())")
    UserDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    User toEntity(UserDto userDto);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "status", expression = "java(user.getStatus().name())")
    @Mapping(target = "gamerStats", source = "gamerStats")
    UserDto toDto(User user, GamerStats gamerStats);


    GamerStatsDto toStatsDto(GamerStats stats);
}

