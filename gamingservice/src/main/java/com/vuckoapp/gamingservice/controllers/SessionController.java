package com.vuckoapp.gamingservice.controllers;

import com.vuckoapp.gamingservice.dto.CreateSessionRequest;
import com.vuckoapp.gamingservice.dto.JwtUserPrincipal;
import com.vuckoapp.gamingservice.services.SeasonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> createSession(@Valid @RequestBody CreateSessionRequest request) {
        return seasonService.createSessionIfUserPermitted(request);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{sessionId}/join")
    public ResponseEntity<?> joinSession(@PathVariable UUID sessionId,@AuthenticationPrincipal JwtUserPrincipal principal) {
        return seasonService.joinSessionIfUserPermitted(sessionId,UUID.fromString(principal.id()));
    }
}
