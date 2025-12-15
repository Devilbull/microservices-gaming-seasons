package com.vuckoapp.notificationservice.service;

import com.vuckoapp.notificationservice.model.NotificationLog;
import com.vuckoapp.notificationservice.repository.NotificationLogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNotificationService {
    private final NotificationLogsRepository notificationLogsRepository;
    @Transactional
    public List<NotificationLog> getAllNotificationsForUser(String email) {
        return notificationLogsRepository.findByEmail(email);

    }
}
