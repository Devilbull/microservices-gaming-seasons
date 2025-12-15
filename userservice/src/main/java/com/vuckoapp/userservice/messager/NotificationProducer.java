package com.vuckoapp.userservice.messager;

import com.vuckoapp.userservice.config.RabbitProducerConfig;
import com.vuckoapp.userservice.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendActivationEmail(String email, String activationToken) {
        NotificationRequest request = NotificationRequest.builder()
                .email(email)
                .subject("Activate your account")
                .message("Click to activate: http://localhost:8081/auth/activate?token=" + activationToken)
                .build();

        rabbitTemplate.convertAndSend(
                RabbitProducerConfig.EXCHANGE,
                RabbitProducerConfig.ACTIVATION_ROUTING_KEY,
                request
        );
    }
}
