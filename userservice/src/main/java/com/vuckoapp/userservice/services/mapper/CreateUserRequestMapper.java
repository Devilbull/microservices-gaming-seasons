package com.vuckoapp.userservice.services.mapper;

import com.vuckoapp.userservice.dto.CreateUserRequest;
import com.vuckoapp.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateUserRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(com.vuckoapp.userservice.model.Role.valueOf(req.role()))")
    @Mapping(target = "status", expression = "java(com.vuckoapp.userservice.model.UserStatus.INITIALIZED)")
    @Mapping(target = "isActivated", constant = "false")
    User toEntity(CreateUserRequest req);
}

