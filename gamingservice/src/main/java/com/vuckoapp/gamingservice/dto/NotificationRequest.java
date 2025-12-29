package com.vuckoapp.gamingservice.dto;

import com.vuckoapp.gamingservice.types.GamingNotificationType;
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
    private GamingNotificationType type;     // ACTIVATION_EMAIL, PASSWORD_RESET
    private String toEmail;             // kome se šalje

    private Map<String, Object> payload; // podaci za template
}


