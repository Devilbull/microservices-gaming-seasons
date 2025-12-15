package com.vuckoapp.userservice.dto;

import com.vuckoapp.userservice.model.User;
import com.vuckoapp.userservice.types.UserNotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {

    private String sourceService;      // USER_SERVICE, GAMING_SERVICE
    private UserNotificationType type;     // ACTIVATION_EMAIL, PASSWORD_RESET
    private String toEmail;             // kome se šalje

    private Map<String, Object> payload; // podaci za template
}


