package com.vuckoapp.userservice.dto;

public record LoginRequest(
        String username,
        String password
) {}
