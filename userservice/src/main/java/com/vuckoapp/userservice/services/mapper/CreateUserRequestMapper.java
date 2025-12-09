
package com.vuckoapp.userservice.services.mapper;

import com.vuckoapp.userservice.dto.CreateUserRequest;
import com.vuckoapp.userservice.model.Role;
import com.vuckoapp.userservice.model.User;
import com.vuckoapp.userservice.model.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CreateUserRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(com.vuckoapp.userservice.model.Role.GAMER)")
    @Mapping(target = "status", expression = "java(com.vuckoapp.userservice.model.UserStatus.INITIALIZED)")
    @Mapping(target = "isActivated", constant = "false")
    User toEntity(CreateUserRequest req);
}
