package com.vuckoapp.notificationservice.controller;

import com.vuckoapp.notificationservice.dto.NotificationRequest;
import com.vuckoapp.notificationservice.service.NotificationService;
import com.vuckoapp.notificationservice.types.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final NotificationService notificationService;

    @GetMapping("/test-mail")
    public void test() {
        NotificationRequest req = new NotificationRequest(
                "aca.vdj@gmail.com",
                NotificationType.ACTIVATION_EMAIL,
                "notification-service",
                Map.of("name", "Vucko")
        );
        notificationService.sendNotification(req);
    }


}