package com.vuckoapp.gamingservice.services;

import com.vuckoapp.gamingservice.dto.*;
import com.vuckoapp.gamingservice.feigncalls.UserserviceCalls;
import com.vuckoapp.gamingservice.messager.NotificationProducer;
import com.vuckoapp.gamingservice.model.*;
import com.vuckoapp.gamingservice.repository.GameRepository;
import com.vuckoapp.gamingservice.repository.ParticipationRepository;
import com.vuckoapp.gamingservice.repository.SessionInviteRepository;
import com.vuckoapp.gamingservice.repository.SessionRepository;
import com.vuckoapp.gamingservice.utils.ResponseBuilder;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final UserserviceCalls userserviceCalls;

    private final SessionRepository sessionRepository;
    private final ParticipationRepository participationRepository;
    private final GameRepository gameRepository;
    private final UserServiceRetry userServiceRetry;
    private final NotificationProducer notificationProducer;
    private final SessionMapper sessionMapper;
    private final SessionInviteRepository sessionInviteRepository;

    public Page<SessionDto> getAllSessions(SessionSearchDto req, Pageable pageable, UUID currentUserId) {
        Specification<Session> spec = Specification.where(null);


        if (req.gameName() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("gameName"), req.gameName()));
        }


        if (req.sessionType() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("sessionType"), req.sessionType()));
        }


        if (req.maxNumPlayers() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("maxPlayers"), req.maxNumPlayers()));
        }


        if (req.keywords() != null && !req.keywords().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("description")), "%" + req.keywords().toLowerCase() + "%"));
        }


        if (req.isJoined() != null && currentUserId != null) {
            if (req.isJoined()) {
                spec = spec.and((root, query, cb) -> {
                    Join<Session, Participation> participants = root.join("participants");
                    return cb.equal(participants.get("userId"), currentUserId);
                });
            } else {
                spec = spec.and((root, query, cb) -> {
                    jakarta.persistence.criteria.Subquery<UUID> subquery = query.subquery(UUID.class);
                    jakarta.persistence.criteria.Root<Participation> pRoot = subquery.from(Participation.class);
                    subquery.select(pRoot.get("sessionId"))
                            .where(cb.equal(pRoot.get("userId"), currentUserId));
                    return cb.not(root.get("id").in(subquery));
                });
            }
        }

        return sessionRepository.findAll(spec, pageable)
                .map(session -> {
                    SessionDto dto = sessionMapper.toDto(session);

                    boolean joined = false;
                    if (currentUserId != null && session.getParticipants() != null) {
                        joined = session.getParticipants().stream()
                                .anyMatch(p -> p.getUserId().equals(currentUserId));
                    }

                    return new SessionDto(
                            dto.sessionId(),
                            dto.creatorId(),
                            dto.sessionName(),
                            dto.gameName(),
                            dto.description(),
                            dto.sessionType(),
                            dto.maxPlayers(),
                            dto.currentPlayers(),
                            dto.startTime(),
                            joined,
                            dto.sessionStatus()
                    );
                });
    }


    public ResponseEntity<?> createSessionIfUserPermitted(CreateSessionRequest request, String email, String username) {


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
            ///  Email notification
            notificationProducer.sendMailThatInformsUserHasLowerAttendance(email,username);
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

    public ResponseEntity<?>    joinSessionIfUserPermitted(UUID sessionID, UUID userID, String email, String username) {
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
        // check if owner
        if(session.getCreatorId().equals(userID)){
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "Owner cannot join their own session");
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
                .userId(userID).sessionId(sessionID).email(email).build();

        participationRepository.save(participant);
        ///  To do sendMail
        notificationProducer.sendMailIfUserHasJoinedSession(email,username,session.getSessionName());
        return ResponseBuilder.build(HttpStatus.OK, "User joined session successfully");

    }

    public ResponseEntity<?> cancelSession(UUID sessionID, UUID userID, String email, String username, String role) {



        // check season exists and is able to canceled
        Session session = sessionRepository.findById(sessionID).orElseThrow(() ->
                new RuntimeException("Session not found"));
        if(session.getSessionStatus() != SessionStatus.SCHEDULED ){
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "Session is cancelled or already finished");
        }

        // check if admin or owner
        if(!session.getCreatorId().equals(userID) && !role.equals("ADMIN")){
            return ResponseBuilder.build(HttpStatus.FORBIDDEN, "Only owner or admin can cancel the session");
        }
        // set cancelled
        session.setSessionStatus(SessionStatus.CANCELLED);
        sessionRepository.save(session);
        // Notify participants
        ArrayList<String> emails = new ArrayList<>();

        for(Participation p : participationRepository.findAllBySessionId(sessionID)){
            emails.add(p.getEmail());
        }
        ///  To do sendMail
        notificationProducer.sendMailToNotifyUsersThatSeasionIsCanceled(emails,session.getSessionName());
        return ResponseBuilder.build(HttpStatus.OK, "Session cancelled successfully");

    }

    public ResponseEntity<?> callToSession(InviteRequest request, UUID organizerId){
            Session session = sessionRepository.findById(request.sessionId()).orElseThrow(() ->
                    new RuntimeException("Session not found"));

            if(!session.getCreatorId().equals(organizerId)){
                throw new RuntimeException("Only the organizer can invite players");
            }

            String token = UUID.randomUUID().toString();
        UserDto user = userServiceRetry.getUserById(request.invitedUserId());

        SessionInvite sessionInvite = SessionInvite.builder()
                .sessionId(request.sessionId())
                .invitedUserId(request.invitedUserId())
                .token(token).used(false).build();

        sessionInviteRepository.save(sessionInvite);





        notificationProducer.sendSessionInvitationMailToUser(
                user.email(),
                user.username(),
                session.getSessionName(),
                token
        );

        return ResponseBuilder.build(HttpStatus.OK, "Session invitation successfully");
    }

    public ResponseEntity<?> acceptInvite(String token) {
        SessionInvite sessionInvite = sessionInviteRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid invitation token"));

        if (sessionInvite.isUsed()) {
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "This invitation has already been used");
        }



        if (sessionInvite.getExpiryDate() != null && sessionInvite.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseBuilder.build(HttpStatus.GONE, "This invitation has expired");
        }

        Session session = sessionRepository.findById(sessionInvite.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session no longer exists"));

        if (session.getParticipants().size() >= session.getMaxPlayers()) {
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "Session is full");
        }
        UUID userId = sessionInvite.getInvitedUserId() ;



        if (participationRepository.existsByUserIdAndSessionId(userId, session.getId())) {
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "User already joined this session");
        }

        UserDto user = userServiceRetry.getUserById(userId);

        Participation participation = Participation.builder()
                .userId(userId)
                .sessionId(session.getId())
                .email(user.email())
                .build();
        participationRepository.save(participation);

        sessionInvite.setUsed(true);
        sessionInviteRepository.save(sessionInvite);

        userServiceRetry.increaseNumberOfSeasonsJoined(userId);

        return ResponseBuilder.build(HttpStatus.OK, "Successfully joined the session via invite");
    }

    @Transactional
    public ResponseEntity<?> lockSession(SessionLockDto sessionLockDto, UUID uuid) {
        Session session = sessionRepository.findById(sessionLockDto.sessionId()).orElse(null);

        if (session == null) {
            return ResponseBuilder.build(HttpStatus.NOT_FOUND, "Session not found");
        }

        if(!session.getCreatorId().equals(uuid)){
            return ResponseBuilder.build(HttpStatus.FORBIDDEN, "Only owner can can lock the session");
        }

        if(session.getSessionStatus() == SessionStatus.FINISHED){
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "Session has already been finished");

        }

        if(session.getParticipants().isEmpty()){
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "Session cannot be empty");
        }

        List<String> presentUsers = new ArrayList<>();
        List<String> absentUsers = new ArrayList<>();

        for(Participation p : session.getParticipants()){
            if(sessionLockDto.emails().contains(p.getEmail())){
                presentUsers.add(p.getEmail());
            }else{
                absentUsers.add(p.getEmail());
            }
        }

        if(presentUsers.isEmpty()){
            return ResponseBuilder.build(HttpStatus.BAD_REQUEST, "Cannot finish session with 0 attendees");
        }

        SessionAttendanceDto sessionAttendanceDto = new SessionAttendanceDto(
                uuid,
                presentUsers,
                absentUsers
        );

        userServiceRetry.processSessionAttendance(sessionAttendanceDto);


        session.setSessionStatus(SessionStatus.FINISHED);
        sessionRepository.save(session);


        return ResponseBuilder.build(HttpStatus.OK, "Session has been successfully locked");
    }


    public Page<SessionDto> getSessionsCreatedByUser(UUID creatorId, Pageable pageable) {
        return sessionRepository
                .findAllByCreatorId(
                        creatorId,
                        pageable
                )
                .map(sessionMapper::toDto);
    }


//    public Page<SessionDto> getSessionsCreatedByUserExcluding(UUID creatorId, UUID excludedUserId, Pageable pageable) {
//        Page<Session> sessionsPage = sessionRepository.findAllByCreatorIdAndSessionStatus(
//                creatorId,
//                SessionStatus.SCHEDULED,
//                pageable
//        );
//
//        List<Session> filtered = sessionsPage.stream()
//                .filter(s -> excludedUserId == null ||
//                        !participationRepository.existsByUserIdAndSessionId(excludedUserId, s.getId()))
//                .toList();
//
//        List<SessionDto> dtos = filtered.stream()
//                .map(sessionMapper::toDto)
//                .toList();
//
//        return new PageImpl<>(dtos, pageable, filtered.size());
//    }

    public Page<SessionDto> getSessionsCreatedByUserExcluding(UUID creatorId, UUID excludedUserId, Pageable pageable) {
        Page<Session> page = sessionRepository.findAllByCreatorIdExcludingUser(
                creatorId,  excludedUserId, pageable
        );
        return page.map(sessionMapper::toDto);
    }
}
