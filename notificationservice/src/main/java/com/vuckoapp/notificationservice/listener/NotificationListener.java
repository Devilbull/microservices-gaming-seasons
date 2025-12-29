package com.vuckoapp.notificationservice.listener;

import com.vuckoapp.notificationservice.config.RabbitConfig;
import com.vuckoapp.notificationservice.dto.NotificationRequest;
import com.vuckoapp.notificationservice.service.NotificationService;
import com.vuckoapp.notificationservice.types.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void receive(NotificationRequest request) {
        if(request.getType() == NotificationType.SESSION_CANCELLATION){
            notificationService.sendEmailsForSessionCancellation(request);
        }else{
            notificationService.sendNotification(request);
        }

    }
}

