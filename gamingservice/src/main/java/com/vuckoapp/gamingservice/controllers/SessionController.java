package com.vuckoapp.gamingservice.controllers;

import com.vuckoapp.gamingservice.dto.*;
import com.vuckoapp.gamingservice.services.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService seasonService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create-session")
    public ResponseEntity<?> createSession(
            @Valid @RequestBody CreateSessionRequest request,@AuthenticationPrincipal JwtUserPrincipal principal) {
        return seasonService.createSessionIfUserPermitted(request, principal.email(),principal.username());
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{sessionId}/join")
    public ResponseEntity<?> joinSession(@PathVariable UUID sessionId,@AuthenticationPrincipal JwtUserPrincipal principal) {
        return seasonService.joinSessionIfUserPermitted(sessionId,UUID.fromString(principal.id()), principal.email(),principal.username());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{sessionId}/cancel")
    public ResponseEntity<?> cancelSession(@PathVariable UUID sessionId,@AuthenticationPrincipal JwtUserPrincipal principal) {
        return seasonService.cancelSession(sessionId,UUID.fromString(principal.id()), principal.email(),principal.username(), principal.role());
    }
//     without parameter isJoined   -----------all sesions
//      isJoined = true	 --------   just my sessions
//            isJoined = false	----   sessions where i am not joined
    @GetMapping("/all")
    public Page<SessionDto> getAllSessions(
            SessionSearchDto searchRequest,
            Pageable pageable,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ){
        UUID currentUserId = (principal != null) ? UUID.fromString(principal.id()) : null;
        return seasonService.getAllSessions(searchRequest, pageable, currentUserId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/call-to-session")
    public ResponseEntity<?> callToSession(
            @RequestBody InviteRequest inviteRequest,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ){
        return seasonService.callToSession(inviteRequest,UUID.fromString(principal.id()));
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/accept-invite")
    public ResponseEntity<?> acceptInvite(
            @RequestParam String token) {
        return seasonService.acceptInvite(token);
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/lock-session")
    public ResponseEntity<?> lockSession(
            @RequestBody SessionLockDto sessionLockDto,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ){
        return seasonService.lockSession(sessionLockDto,UUID.fromString(principal.id()));
    }





    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    public Page<SessionDto> getMySessions(
            @RequestParam(required = false) UUID excludedUserId,
            Pageable pageable,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ) {

        UUID userId = UUID.fromString(principal.id());
        if(excludedUserId == null){
            return seasonService.getSessionsCreatedByUser(userId, pageable);
        }
        return seasonService.getSessionsCreatedByUserExcluding(userId, excludedUserId, pageable);
    }





}
