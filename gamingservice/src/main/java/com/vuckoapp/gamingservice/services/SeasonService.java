package com.vuckoapp.gamingservice.services;

import com.vuckoapp.gamingservice.dto.CreateSessionRequest;
import com.vuckoapp.gamingservice.dto.SessionEligibilityDto;
import com.vuckoapp.gamingservice.dto.UserDto;
import com.vuckoapp.gamingservice.feigncalls.UserserviceCalls;
import com.vuckoapp.gamingservice.model.Participation;
import com.vuckoapp.gamingservice.model.Session;
import com.vuckoapp.gamingservice.model.SessionStatus;
import com.vuckoapp.gamingservice.model.SessionType;
import com.vuckoapp.gamingservice.repository.GameRepository;
import com.vuckoapp.gamingservice.repository.ParticipationRepository;
import com.vuckoapp.gamingservice.repository.SessionRepository;
import com.vuckoapp.gamingservice.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeasonService {
    private final UserserviceCalls userserviceCalls;

    private final SessionRepository sessionRepository;
    private final ParticipationRepository participationRepository;
    private final GameRepository gameRepository;
    private final UserServiceRetry userServiceRetry;

    public ResponseEntity<?> createSessionIfUserPermitted(CreateSessionRequest request) {


        // Check if game exists
        if(!gameRepository.existsByGameName(request.gameName())) {
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "Game does not exist");
        }
        // Check season name
        if(sessionRepository.existsBySessionName(request.sessionName())) {
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "Session name already exists");
        }

        // Check if can make session
        SessionEligibilityDto eligibility = userServiceRetry.getEligibilityStats();

        if (eligibility.blocked()) {
            return ResponseBuilder.build(HttpStatus.FORBIDDEN, "User is blocked");
        }

        if (!eligibility.attendanceOk()) {
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "Attendance must be >= 90%");
        }

        //  Get user info
        UserDto user = userServiceRetry.getUserInfo();

        // Build session
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

        // S ave  session
        sessionRepository.save(session);

        //
        return ResponseBuilder.build(HttpStatus.OK, "Gaming session created successfully");
    }

    public ResponseEntity<?> joinSessionIfUserPermitted(UUID sessionID, UUID userID) {
        // check if blocked
        SessionEligibilityDto eligibility = userServiceRetry.getEligibilityStats();

        if (eligibility.blocked()) {
            return ResponseBuilder.build(HttpStatus.FORBIDDEN, "User is blocked");
        }
        // check season exists and is open
        Session session = sessionRepository.findById(sessionID).orElseThrow(() ->
                new RuntimeException("Session not found"));
        if(session.getSessionStatus() != SessionStatus.SCHEDULED || session.getSessionType() != SessionType.OPEN){
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "Session is not open for joining");
        }
        // check max players not exceeded
        if(session.getNumberOfJoinedPlayers() >= session.getMaxPlayers()){
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "Session is full");
        }
        // check if already joined
        if(participationRepository.existsByUserIdAndSessionId(userID, sessionID)){
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "User already joined this session");
        }
        // Increase number of joined season for user in UserService
        userServiceRetry.increaseNumberOfSeasonsJoined(userID);

        // increase
        session.setNumberOfJoinedPlayers(session.getNumberOfJoinedPlayers() + 1);
        sessionRepository.save(session);

        // Add to participants
        Participation participant = Participation.builder()
                .userId(userID).sessionId(sessionID).build();

        participationRepository.save(participant);
        return ResponseBuilder.build(HttpStatus.OK, "User joined session successfully");

    }
}
