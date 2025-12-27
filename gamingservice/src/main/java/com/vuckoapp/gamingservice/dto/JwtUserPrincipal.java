package com.vuckoapp.gamingservice.dto;

public record JwtUserPrincipal(
        String id,
        String username,
        String email,
        String role
) {}