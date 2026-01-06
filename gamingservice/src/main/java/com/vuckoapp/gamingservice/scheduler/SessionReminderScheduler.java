package com.vuckoapp.gamingservice.scheduler;

import com.vuckoapp.gamingservice.messager.NotificationProducer;
import com.vuckoapp.gamingservice.model.Participation;
import com.vuckoapp.gamingservice.model.Session;
import com.vuckoapp.gamingservice.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SessionReminderScheduler {
    private final SessionRepository sessionRepository;
    private final NotificationProducer notificationProducer;

    @Transactional
    @Scheduled(fixedRate = 60_000)
     public void sendSessionReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.plusMinutes(59);
        LocalDateTime to = now.plusMinutes(60);
        List<Session> sessions = sessionRepository.findSessionsStartingBetween( from, to );
        for (Session session : sessions) {

            List<String> emails = session.getParticipants()
                    .stream()
                    .map(Participation::getEmail)
                    .toList();

            if (!emails.isEmpty()) {
                notificationProducer.sendMailToNotifyUsersThatSessionIsin60Mins(
                        emails,
                        session.getSessionName(),
                        session.getStartOfSession()
                );
            }
            session.setReminder60Sent(true);
        }
    }
}

