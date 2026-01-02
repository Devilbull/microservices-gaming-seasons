package com.vuckoapp.userservice.services;

import com.vuckoapp.userservice.dto.SessionAttendanceDto;
import com.vuckoapp.userservice.messager.NotificationProducer;
import com.vuckoapp.userservice.model.GamerStats;
import com.vuckoapp.userservice.model.OrganizerTitle;
import com.vuckoapp.userservice.model.User;
import com.vuckoapp.userservice.repository.GamerStatsRepository;
import com.vuckoapp.userservice.repository.UserRepository;
import com.vuckoapp.userservice.utils.ResponseBuilder;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameServiceRequestsService {
    private final UserRepository userRepository;
    private final GamerStatsRepository gamerStatsRepository;
    private final NotificationProducer notificationProducer;
    public ResponseEntity<?> increaseNumberOfSeasonsJoined(UUID userId) {
        // Does he exist
        if(userRepository.existsById(userId)){

            GamerStats gamerStats  = gamerStatsRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Gamer stats not found for user"));

            gamerStats.setTotalSessions(gamerStats.getTotalSessions() + 1);
            gamerStatsRepository.save(gamerStats);
            return ResponseBuilder.build(HttpStatus.OK, "User joined session successfully");

        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }
    @Transactional
    public ResponseEntity<?> updateAttendance(SessionAttendanceDto sessionAttendanceDto) {
        UUID userId = sessionAttendanceDto.organizerId();
        ArrayList<String> attendMails = new ArrayList<>(sessionAttendanceDto.presentUsers());
        ArrayList<String> absentMails = new ArrayList<>(sessionAttendanceDto.absentUsers());

        for (String email : attendMails) {
             Optional<User> u = userRepository.findByEmail(email);
             if(u.isPresent()){
                 GamerStats gamerStats  = gamerStatsRepository.findByUserId(u.get().getId())
                         .orElseThrow(() -> new RuntimeException("Gamer stats not found for user"));

                 gamerStats.setNumberOfJoinedSessions(gamerStats.getNumberOfJoinedSessions() + 1);
                 // Calculate attendance rate
                 gamerStats.setAttendanceNumber(BigDecimal.valueOf((gamerStats.getNumberOfJoinedSessions())*100. / (gamerStats.getNumberOfJoinedSessions() + gamerStats.getNumberOfLeavingSessions())));
                 gamerStatsRepository.save(gamerStats);
             }

        }
        for (String email : absentMails) {
            Optional<User> u = userRepository.findByEmail(email);
            if(u.isPresent()){
                GamerStats gamerStats  = gamerStatsRepository.findByUserId(u.get().getId())
                        .orElseThrow(() -> new RuntimeException("Gamer stats not found for user"));

                gamerStats.setNumberOfLeavingSessions(gamerStats.getNumberOfLeavingSessions() + 1);
                gamerStats.setAttendanceNumber(BigDecimal.valueOf((gamerStats.getNumberOfJoinedSessions())*100. / (gamerStats.getNumberOfJoinedSessions() + gamerStats.getNumberOfLeavingSessions())));
                gamerStatsRepository.save(gamerStats);
            }
        }
        // Owner update
        GamerStats organizerStats  = gamerStatsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Gamer stats not found for user"));
        organizerStats.setNumberOfSuccessfulSessions(organizerStats.getNumberOfSuccessfulSessions() + 1);
        // Update title
        OrganizerTitle newTitle = resolveOrganizerTitle(
                organizerStats.getNumberOfSuccessfulSessions()
        );
        OrganizerTitle oldTitle = organizerStats.getOrganizerTitle();
        organizerStats.setOrganizerTitle(newTitle);

        gamerStatsRepository.save(organizerStats);

        if(!newTitle.equals(oldTitle)){
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            notificationProducer.sendMailUpdatedTitle(
                    user.getEmail(),
                    user.getUsername(),
                    newTitle
            );
        }

        return ResponseBuilder.build(HttpStatus.OK, "Attendance updated successfully");


    }

    private OrganizerTitle resolveOrganizerTitle(int successfulSessions) {
        if (successfulSessions <= 0) {
            return OrganizerTitle.BRONZE;
        } else if (successfulSessions <= 10) {
            return OrganizerTitle.SILVER;
        } else if (successfulSessions <= 30) {
            return OrganizerTitle.GOLD;
        } else if (successfulSessions <= 60) {
            return OrganizerTitle.PLATINUM;
        } else {
            return OrganizerTitle.DIAMOND;
        }
    }
}
