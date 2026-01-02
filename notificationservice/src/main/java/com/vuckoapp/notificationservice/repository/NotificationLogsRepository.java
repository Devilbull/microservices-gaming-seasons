package com.vuckoapp.notificationservice.repository;

import com.vuckoapp.notificationservice.model.NotificationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.management.Notification;
import java.util.List;
import java.util.UUID;

public interface NotificationLogsRepository extends JpaRepository<NotificationLog, Long> {

    List<NotificationLog> findByEmail(String email);

    Page<NotificationLog> findByEmail(String email, Pageable pageable);


    Page<NotificationLog> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}
