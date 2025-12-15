package com.vuckoapp.notificationservice.controller;

import com.vuckoapp.notificationservice.dto.NotificationRequest;
import com.vuckoapp.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final NotificationService notificationService;

    @GetMapping("/test-mail")
    public void test() {
        NotificationRequest req = new NotificationRequest(
                "aca.vdj@gmail.com",
                "Test",
                "Radi 👍"
        );
        notificationService.sendNotification(req);
    }


}