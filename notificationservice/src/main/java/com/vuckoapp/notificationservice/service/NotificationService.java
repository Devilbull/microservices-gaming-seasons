package com.vuckoapp.notificationservice.service;

import com.vuckoapp.notificationservice.dto.NotificationRequest;
import com.vuckoapp.notificationservice.model.NotificationLog;
import com.vuckoapp.notificationservice.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final NotificationLogRepository logRepository;

    public void sendNotification(NotificationRequest request) {
        // 1. Pošalji email
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(request.getEmail());
        mail.setSubject(request.getSubject());
        mail.setText(request.getMessage());
        mailSender.send(mail);

        // 2. Arhiviraj u bazu
        NotificationLog log = new NotificationLog();
        log.setEmail(request.getEmail());
        log.setSubject(request.getSubject());
        log.setMessage(request.getMessage());
        log.setSentAt(LocalDateTime.now());
        logRepository.save(log);
    }
}
