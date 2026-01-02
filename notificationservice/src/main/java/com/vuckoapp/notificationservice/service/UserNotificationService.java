package com.vuckoapp.notificationservice.service;

import com.vuckoapp.notificationservice.model.NotificationLog;
import com.vuckoapp.notificationservice.repository.NotificationLogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserNotificationService {
    private final NotificationLogsRepository notificationLogsRepository;
    @Transactional(readOnly = true)
    public Page<NotificationLog> getAllNotificationsForUser(
            String email,
            Pageable pageable
    ) {
        return notificationLogsRepository.findByEmail(email, pageable);
    }

    @Transactional
    public Page<NotificationLog> getAllNotifications(String email, Pageable pageable) {
        Page<NotificationLog> page;
        if (email != null && !email.isBlank()) {
            page = notificationLogsRepository.findByEmailContainingIgnoreCase(email, pageable);
        } else {
            page = notificationLogsRepository.findAll(pageable);
        }
        return page;
    }
}
