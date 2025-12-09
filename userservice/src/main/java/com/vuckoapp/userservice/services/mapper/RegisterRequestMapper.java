// java
package com.vuckoapp.userservice.services.mapper;

import com.vuckoapp.userservice.dto.RegisterRequest;
import com.vuckoapp.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RegisterRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", expression = "java(com.vuckoapp.userservice.model.Role.GAMER)")
    @Mapping(target = "status", expression = "java(com.vuckoapp.userservice.model.UserStatus.INITIALIZED)")
    User toUser(RegisterRequest request);
}
