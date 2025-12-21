package com.vuckoapp.notificationservice.service;

import com.vuckoapp.notificationservice.dto.NotificationRequest;
import com.vuckoapp.notificationservice.model.NotificationLog;
import com.vuckoapp.notificationservice.repository.NotificationLogsRepository;
import com.vuckoapp.notificationservice.types.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final NotificationLogsRepository logRepository;

    public void sendNotification(NotificationRequest request) {

        // 1️⃣ Na osnovu type + payload formiraj subject + body
        String subject = resolveSubject(request.getType());
        String body = resolveBody(request.getType(), request.getPayload());

        // 2️⃣ Pošalji mail
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(request.getToEmail());
        mail.setSubject(subject);
        mail.setText(body);
        mailSender.send(mail);

        // 3️⃣ Sačuvaj u bazi
        NotificationLog log = new NotificationLog();
        log.setEmail(request.getToEmail());
        log.setSubject(subject);
        log.setMessage(body);
        log.setSentAt(LocalDateTime.now());

        logRepository.save(log);
    }


    private String resolveSubject(NotificationType type) {
        return switch (type) {
            case ACTIVATION_EMAIL -> "Account Activation";
            case PASSWORD_RESET -> "Password Reset";
            case SESSION_INVITATION -> "Session Invitation";
            default -> "System Notification";
        };
    }

    private String resolveBody(
            NotificationType type,
            Map<String, Object> payload
    ) {
        return switch (type) {
            case ACTIVATION_EMAIL -> {
                String username = (String) payload.get("username");
                String token = (String) payload.get("activationToken");
                String localUrl = "http://localhost:8080";
                yield """
                   Hello %s,
                   
                   Click the link below to activate your account:
                   %s/api/userservice/auth/activate?token=%s
            
                   Regards,
                   Team
                   """.formatted(username,localUrl, token);
            }
            case PASSWORD_RESET -> {
                String username = (String) payload.get("username");
                String token = (String) payload.get("passwordResetToken");

                yield """
                   Hello %s,
                   
                   Forgot your password? No worries.
                   Here is your temporary token: %s
                
                   This token expires in 15 minutes.
                   If you did not request this, feel free to ignore this email.
                
                   Regards,
                   Team
                   """.formatted(username, token);
            }

            default -> "New notification.";
        };
    }


}

