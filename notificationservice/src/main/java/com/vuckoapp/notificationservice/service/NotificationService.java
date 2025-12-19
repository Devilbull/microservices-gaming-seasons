package com.vuckoapp.notificationservice.service;

import com.vuckoapp.notificationservice.dto.NotificationRequest;
import com.vuckoapp.notificationservice.model.NotificationLog;
import com.vuckoapp.notificationservice.repository.NotificationLogRepository;
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
    private final NotificationLogRepository logRepository;

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
            case ACTIVATION_EMAIL -> "Aktivacija naloga";
            case PASSWORD_RESET -> "Reset lozinke";
            case SESSION_INVITATION -> "Pozivnica za sesiju";
            default -> "Sistemska notifikacija";
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

                yield """
                    Zdravo %s,

                    Klikni na link ispod da aktiviraš nalog:
                    http://localhost:8081/api/userservice/auth/activate?token=%s

                    Pozdrav,
                    Tim
                    """.formatted(username, token);
            }
            case PASSWORD_RESET -> {
                String username = (String) payload.get("username");
                String token = (String) payload.get("passwordResetToken");

                yield """
                    Zdravo %s,

                    Zaboravili ste lozinku? Nema problema.
                    Ovo je vas privremeni token: %s
                    
                    Ovaj token ističe za 15 minuta.
                    Ako niste vi poslali ovaj zahtev, slobodno ignorišite ovaj mejl.

                    Pozdrav,
                    Tim
                    """.formatted(username, token);
            }

            default -> "Imate novu notifikaciju.";
        };
    }


}

