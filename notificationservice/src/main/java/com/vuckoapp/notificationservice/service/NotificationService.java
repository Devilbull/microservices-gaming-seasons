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
            case SESSION_JOINED -> "Session Joined";
            case SESSION_CANCELLATION -> "Session Canceled";

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
            case SESSION_JOINED -> {
                String username = (String) payload.get("username");
                String gameName = (String) payload.get("gameName");

                yield """
                   Hello %s,
                   
                   You have successfully joined the session for the game: %s.
                
                   Get ready to play and have fun!
                
                   Regards,
                   Team
                   """.formatted(username, gameName);
            }
            case SESSION_CANCELLATION -> {
                String sessionName = (String) payload.get("sessionName");
                yield """
                   Hello,
                   
                   The gaming session "%s" has been canceled.
                   
                   We apologize for the inconvenience.
                   
                   Regards,
                   Team
                   """.formatted(sessionName);
            }
            case SESSION_INVITATION ->{
                String username = (String) payload.get("username");
                String sessionName = (String) payload.get("sessionName");
                String inviteLink = (String) payload.get("inviteLink");

                yield """
                   Hello %s,
                   
                   You have been invited to join the gaming session: %s.
                   
                   Click the link below to accept the invitation:
                   %s
                   
                   Regards,
                   Team
                   """.formatted(username, sessionName, inviteLink);
            }
            default -> "New notification.";
        };
    }


    @SuppressWarnings("unchecked")
    public void sendEmailsForSessionCancellation(NotificationRequest request) {

        Object emailsObj = request.getPayload().get("emails");
        if (emailsObj == null) {
            throw new IllegalArgumentException("Emails list is missing in payload");
        }

        var emails = (java.util.List<String>) emailsObj;

        for (String email : emails) {
            NotificationRequest singleRequest = NotificationRequest.builder()
                    .toEmail(email)
                    .type(NotificationType.SESSION_CANCELLATION)
                    .sourceService(request.getSourceService())
                    .payload(request.getPayload())
                    .build();

            sendNotification(singleRequest);
        }
    }
}

