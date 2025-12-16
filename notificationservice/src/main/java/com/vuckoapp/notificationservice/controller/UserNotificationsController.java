package com.vuckoapp.notificationservice.controller;


import com.vuckoapp.notificationservice.model.NotificationLog;
import com.vuckoapp.notificationservice.service.UserNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class UserNotificationsController {

    private final UserNotificationService userNotificationService;

    @GetMapping("/my")
    public ResponseEntity<?> myNotifications(Authentication auth) {
        String email = (String) auth.getDetails();
        List<NotificationLog> notifications = userNotificationService.getAllNotificationsForUser(email);
        return ResponseEntity.ok(notifications);
    }
    @GetMapping("/all")
    public ResponseEntity<?> allNotifications() {

        List<NotificationLog> notifications = userNotificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }
}
