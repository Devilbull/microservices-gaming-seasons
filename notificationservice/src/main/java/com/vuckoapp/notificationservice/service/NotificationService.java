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

        // Get data
        String subject = resolveSubject(request.getType());
        String body = resolveBody(request.getType(), request.getPayload());

        // Send email
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(request.getToEmail());
        mail.setSubject(subject);
        mail.setText(body);
        mailSender.send(mail);

        // Save in db
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
            case RANK_CHANGED -> "Organizer Title Updated";
            case SESSION_CREATION_REJECTED -> "Session Creation Rejected";
            case SESSION_REMINDER_60_MIN -> "Session Reminder: Starting in 60 Minutes";
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
            case RANK_CHANGED -> {
                String username = (String) payload.get("username");
                String newTitle = (String) payload.get("newTitle");

                yield """
                   Hello %s,
                   
                   Congratulations! Your organizer title has been updated to: %s.
                
                   Keep up the great work!
                
                   Regards,
                   Team
                   """.formatted(username, newTitle);
            }
            case SESSION_CREATION_REJECTED -> {
                String username = (String) payload.get("username");

                yield """
                   Hello %s,
                   
                   We regret to inform you that your session creation request  has been rejected.
                
                   You attendance is less then 90 PERCENT.
                
                   Regards,
                   Team
                   """.formatted(username);
            }
            case SESSION_REMINDER_60_MIN -> {
                String username = (String) payload.get("username");
                String sessionName = (String) payload.get("sessionName");
                String sessionTime = (String) payload.get("sessionTime");
                yield """
                   Hello %s,
                   
                   This is a reminder that your gaming session "%s" is starting in 60 minutes at %s.
                
                   Get ready to join the fun!
                
                   Regards,
                   Team
                   """.formatted(username, sessionName, sessionTime);
            }
            default -> "New notification.";
        };
    }

    public void sendMultipleNotifications(NotificationRequest request) {

        Object emailsObj = request.getPayload().get("emails");
        if (emailsObj == null) {
            throw new IllegalArgumentException("Emails list is missing in payload");
        }

        var emails = (java.util.List<String>) emailsObj;

        NotificationType type = request.getType();

        for (String email : emails) {
            NotificationRequest singleRequest = NotificationRequest.builder()
                    .toEmail(email)
                    .type(type)
                    .sourceService(request.getSourceService())
                    .payload(request.getPayload())
                    .build();

            sendNotification(singleRequest);
        }
    }
}

