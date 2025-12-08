package com.vuckoapp.userservice.services.mapper;

import com.vuckoapp.userservice.model.RegisterRequest;
import com.vuckoapp.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterRequestMapper {
    @Mapping(target = "role", constant = "GAMER")
    @Mapping(target = "status", constant = "INITIALIZED")
    @Mapping(target = "isActivated", constant = "false")
    @Mapping(target = "id", ignore = true)
    User toUser(RegisterRequest request);
}
