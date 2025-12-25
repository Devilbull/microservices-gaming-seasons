package com.vuckoapp.gamingservice.services;

import com.vuckoapp.gamingservice.dto.CreateSessionRequest;
import com.vuckoapp.gamingservice.dto.SessionEligibilityDto;
import com.vuckoapp.gamingservice.dto.UserDto;
import com.vuckoapp.gamingservice.feigncalls.UserserviceCalls;
import com.vuckoapp.gamingservice.model.Session;
import com.vuckoapp.gamingservice.model.SessionStatus;
import com.vuckoapp.gamingservice.model.SessionType;
import com.vuckoapp.gamingservice.repository.SessionRepository;
import com.vuckoapp.gamingservice.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SeasonService {
    private final UserserviceCalls userserviceCalls;

    private final SessionRepository sessionRepository;

    public ResponseEntity<?> createSessionIfUserPermitted(CreateSessionRequest request) {
        // 1️⃣ Poziv user-service da proverimo da li korisnik može da kreira sesiju
        SessionEligibilityDto eligibility = userserviceCalls.canCreateSession();

        if (eligibility.blocked()) {
            return ResponseBuilder.build(HttpStatus.FORBIDDEN, "User is blocked");
        }

        if (!eligibility.attendanceOk()) {
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "Attendance must be >= 90%");
        }

        // 2️⃣ Dobijamo detalje korisnika
        UserDto user = userserviceCalls.getUserInfo();

        // 3️⃣ Kreiranje sesije koristeći podatke iz DTO
        Session session = Session.builder()
                .creatorId(user.id())
                .sessionName(request.sessionName())
                .gameName(request.gameName())
                .maxPlayers(request.maxPlayers())
                .sessionType(request.sessionType())
                .startOfSession(request.startOfSession())
                .description(request.description())
                .sessionStatus(SessionStatus.SCHEDULED)
                .build();

        // 4️⃣ Čuvanje u bazi
        sessionRepository.save(session);

        // 5️⃣ Vraćanje odgovora sa sačuvanom sesijom
        return ResponseBuilder.build(HttpStatus.OK, "Gaming session created successfully");
    }

}
