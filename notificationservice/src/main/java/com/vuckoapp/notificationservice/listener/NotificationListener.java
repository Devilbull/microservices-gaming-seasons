package com.vuckoapp.notificationservice.listener;

import com.vuckoapp.notificationservice.config.RabbitConfig;
import com.vuckoapp.notificationservice.dto.NotificationRequest;
import com.vuckoapp.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receive(NotificationRequest request) {
        notificationService.sendNotification(request);
    }
}

