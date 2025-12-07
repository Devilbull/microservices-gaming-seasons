package com.vuckoapp.userservice.model;

public record LoginRequest(
        String username,
        String password
) {}
