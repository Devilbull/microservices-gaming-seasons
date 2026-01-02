package com.vuckoapp.notificationservice.controller;


import com.vuckoapp.notificationservice.model.NotificationLog;
import com.vuckoapp.notificationservice.service.UserNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Page<NotificationLog>> myNotifications(
            Authentication auth,

            @PageableDefault(size = 10, sort = "sentAt") Pageable pageable
    ) {
        String email = (String) auth.getDetails();
        return ResponseEntity.ok(
                userNotificationService.getAllNotificationsForUser(email, pageable)
        );
    }
    @GetMapping("/all")
    public ResponseEntity<?> allNotifications(
            @RequestParam(required = false) String email,
            @PageableDefault(size = 10, sort = "sentAt") Pageable pageable
    ) {


        return ResponseEntity.ok(userNotificationService.getAllNotifications(email,pageable));
    }
}
