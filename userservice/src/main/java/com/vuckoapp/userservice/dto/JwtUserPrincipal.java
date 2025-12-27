package com.vuckoapp.userservice.dto;

public record JwtUserPrincipal(
        String id,
        String username,
        String email,
        String role
) {}
