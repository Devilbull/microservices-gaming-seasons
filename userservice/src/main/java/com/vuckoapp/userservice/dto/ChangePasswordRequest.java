package com.vuckoapp.userservice.dto;

public record ChangePasswordRequest(
        String oldPassword,
        String newPassword
) {}
