package com.vuckoapp.gamingservice.controllers;

import com.vuckoapp.gamingservice.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;


    @GetMapping("/all")
    public ResponseEntity<?> getAllGames() {
        return gameService.getAllGames();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allAd")
    public ResponseEntity<?> getAllGamesAd() {
        return gameService.getAllGames();
    }
}
