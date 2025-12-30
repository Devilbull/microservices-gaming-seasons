package com.vuckoapp.gamingservice.controllers;

import com.vuckoapp.gamingservice.dto.CreateSessionRequest;
import com.vuckoapp.gamingservice.dto.JwtUserPrincipal;
import com.vuckoapp.gamingservice.dto.SessionDto;
import com.vuckoapp.gamingservice.dto.SessionSearchDto;
import com.vuckoapp.gamingservice.services.SeasonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SeasonService seasonService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create-session")
    public ResponseEntity<?> createSession(@Valid @RequestBody CreateSessionRequest request,@AuthenticationPrincipal JwtUserPrincipal principal) {
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

    @GetMapping("/all")
    public Page<SessionDto> getAllSessions(
            SessionSearchDto searchRequest,
            Pageable pageable,
            @AuthenticationPrincipal JwtUserPrincipal principal
    ){
        UUID currentUserId = (principal != null) ? UUID.fromString(principal.id()) : null;
        return seasonService.getAllSessions(searchRequest, pageable, currentUserId);
    }
}
