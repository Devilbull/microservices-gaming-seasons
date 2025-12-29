package com.vuckoapp.gamingservice.messager;

import com.vuckoapp.gamingservice.config.RabbitProducerConfig;
import com.vuckoapp.gamingservice.dto.NotificationRequest;
import com.vuckoapp.gamingservice.types.GamingNotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMailThatInformsUserHasLowerAttendance(String email, String username) {
        NotificationRequest request = NotificationRequest.builder()
                .toEmail(email)
                .type(GamingNotificationType.SESSION_REJECTED_ATTENDANCE)
                .sourceService("USER_SERVICE")
                .payload(Map.of(
                        "username", username,
                        "email", email
                ))
                .build();

        rabbitTemplate.convertAndSend(
                RabbitProducerConfig.EXCHANGE,
                RabbitProducerConfig.ROUTING_KEY, // ← novi routing key: "notification.send"
                request
        );
    }

    public void sendMailIfUserHasJoinedSession(String email, String username, String sessionName)  {
        NotificationRequest request = NotificationRequest.builder()
                .toEmail(email)
                .type(GamingNotificationType.SESSION_JOINED)
                .sourceService("USER_SERVICE")
                .payload(Map.of(
                        "username", username,
                        "email", email,
                        "sessionName", sessionName
                ))
                .build();

        rabbitTemplate.convertAndSend(
                RabbitProducerConfig.EXCHANGE,
                RabbitProducerConfig.ROUTING_KEY, // ← novi routing key: "notification.send"
                request
        );
    }
//    public void sendPasswordResetEmail(String email,String username, String token) {
//        NotificationRequest request = NotificationRequest.builder()
//                .toEmail(email)
//                .type(GamingNotificationType.PASSWORD_RESET)
//                .sourceService("USER_SERVICE")
//                .payload(Map.of(
//                        "username", username,
//                        "passwordResetToken", token
//                ))
//                .build();
//
//        rabbitTemplate.convertAndSend(
//                RabbitProducerConfig.EXCHANGE,
//                RabbitProducerConfig.ROUTING_KEY,
//                request
//        );
//    }

}
