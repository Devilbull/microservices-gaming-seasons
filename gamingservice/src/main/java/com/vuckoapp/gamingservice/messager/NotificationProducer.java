package com.vuckoapp.gamingservice.messager;

import com.vuckoapp.gamingservice.config.RabbitProducerConfig;
import com.vuckoapp.gamingservice.dto.NotificationRequest;
import com.vuckoapp.gamingservice.types.GamingNotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMailThatInformsUserHasLowerAttendance(String email, String username) {
        NotificationRequest request = NotificationRequest.builder()
                .toEmail(email)
                .type(GamingNotificationType.SESSION_CREATION_REJECTED)
                .sourceService("GAMING_SERVICE")
                .payload(Map.of(
                        "username", username,
                        "email", email
                ))
                .build();

        rabbitTemplate.convertAndSend(
                RabbitProducerConfig.EXCHANGE,
                RabbitProducerConfig.ROUTING_KEY,
                request
        );
    }

    public void sendMailIfUserHasJoinedSession(String email, String username, String sessionName, String gameName)  {
        NotificationRequest request = NotificationRequest.builder()
                .toEmail(email)
                .type(GamingNotificationType.SESSION_JOINED)
                .sourceService("GAMING_SERVICE")
                .payload(Map.of(
                        "username", username,
                        "email", email,
                        "sessionName", sessionName,
                            "gameName", gameName
                ))
                .build();

        rabbitTemplate.convertAndSend(
                RabbitProducerConfig.EXCHANGE,
                RabbitProducerConfig.ROUTING_KEY,
                request
        );
    }

    public void sendMailToNotifyUsersThatSeasionIsCanceled(ArrayList<String> emails, String sessionName) {
        NotificationRequest request = NotificationRequest.builder()
                .toEmail("no email for this type of notification")
                .type(GamingNotificationType.SESSION_CANCELLATION)
                .sourceService("GAMING_SERVICE")
                .payload(Map.of(
                        "emails", emails,
                        "sessionName", sessionName
                ))
                .build();

        rabbitTemplate.convertAndSend(
                RabbitProducerConfig.EXCHANGE,
                RabbitProducerConfig.ROUTING_KEY,
                request
        );
    }

    public void sendSessionInvitationMailToUser(String email, String username, String sessionName, String token) {
        NotificationRequest request = NotificationRequest.builder()
                .toEmail(email)
                .type(GamingNotificationType.SESSION_INVITATION)
                .sourceService("GAMING_SERVICE")
                .payload(Map.of(
                        "username", username,
                        "sessionName", sessionName,
                        "token", token,
                        "inviteLink", "http://localhost:8080/api/gamingservice/sessions/accept-invite?token=" + token
                ))
                .build();

        rabbitTemplate.convertAndSend(
                RabbitProducerConfig.EXCHANGE,
                RabbitProducerConfig.ROUTING_KEY,
                request
        );
    }


    public void sendMailToNotifyUsersThatSessionIsin60Mins(List<String> emails, String sessionName, LocalDateTime startOfSession) {
        NotificationRequest request = NotificationRequest.builder()
                .toEmail("no email for this type of notification")
                .type(GamingNotificationType.SESSION_REMINDER_60_MIN)
                .sourceService("GAMING_SERVICE")
                .payload(Map.of(
                        "emails", emails,
                        "sessionName", sessionName,
                        "sessionTime", startOfSession.toString()
                ))
                .build();

        rabbitTemplate.convertAndSend(
                RabbitProducerConfig.EXCHANGE,
                RabbitProducerConfig.ROUTING_KEY,
                request
        );
    }


}
