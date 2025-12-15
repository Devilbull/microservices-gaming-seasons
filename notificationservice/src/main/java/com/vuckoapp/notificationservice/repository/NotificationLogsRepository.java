package com.vuckoapp.notificationservice.repository;

import com.vuckoapp.notificationservice.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.management.Notification;
import java.util.List;
import java.util.UUID;

public interface NotificationLogsRepository extends JpaRepository<NotificationLog, Long> {

    List<NotificationLog> findByEmail(String email);
}
