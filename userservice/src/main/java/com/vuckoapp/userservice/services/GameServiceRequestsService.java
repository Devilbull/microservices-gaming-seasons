package com.vuckoapp.userservice.services;

import com.vuckoapp.userservice.model.GamerStats;
import com.vuckoapp.userservice.repository.GamerStatsRepository;
import com.vuckoapp.userservice.repository.UserRepository;
import com.vuckoapp.userservice.utils.ResponseBuilder;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameServiceRequestsService {
    private final UserRepository userRepository;
    private final GamerStatsRepository gamerStatsRepository;
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
}
