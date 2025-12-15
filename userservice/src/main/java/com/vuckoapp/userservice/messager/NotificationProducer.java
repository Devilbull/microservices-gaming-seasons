package com.vuckoapp.userservice.messager;

import com.vuckoapp.userservice.config.RabbitProducerConfig;
import com.vuckoapp.userservice.dto.NotificationRequest;
import com.vuckoapp.userservice.types.UserNotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendActivationEmail(String email,String username, String activationToken) {
        NotificationRequest request = NotificationRequest.builder()
                .toEmail(email)
                .type(UserNotificationType.ACTIVATION_EMAIL)
                .sourceService("USER_SERVICE")
                .payload(Map.of(
                        "username", username,
                        "activationToken", activationToken
                ))
                .build();

        rabbitTemplate.convertAndSend(
                RabbitProducerConfig.EXCHANGE,
                RabbitProducerConfig.ROUTING_KEY, // ← novi routing key: "notification.send"
                request
        );
    }
}
