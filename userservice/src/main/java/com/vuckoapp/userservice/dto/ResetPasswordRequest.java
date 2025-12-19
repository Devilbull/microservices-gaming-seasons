package com.vuckoapp.userservice.dto;

public record ResetPasswordRequest(
        String token,
        String newPassword
) {

}
