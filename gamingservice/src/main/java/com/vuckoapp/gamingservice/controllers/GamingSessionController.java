package com.vuckoapp.gamingservice.controllers;

import com.vuckoapp.gamingservice.dto.CreateSessionRequest;
import com.vuckoapp.gamingservice.services.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class GamingSessionController {

    private final SeasonService seasonService;

    @PostMapping("/create")
    public ResponseEntity<?> createSessionIfUserPermitted(@RequestBody CreateSessionRequest request) {
        return seasonService.createSessionIfUserPermitted(request);
    }
}
