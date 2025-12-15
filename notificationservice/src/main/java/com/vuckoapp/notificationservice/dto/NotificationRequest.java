package com.vuckoapp.notificationservice.dto;

import com.vuckoapp.notificationservice.types.NotificationType;
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

    private String toEmail;
    private NotificationType type;
    private String sourceService;
    private Map<String, Object> payload;
}

