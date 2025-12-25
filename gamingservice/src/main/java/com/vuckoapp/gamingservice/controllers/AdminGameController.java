package com.vuckoapp.gamingservice.controllers;

import com.vuckoapp.gamingservice.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/games")
@RequiredArgsConstructor
public class AdminGameController {

    private final GameService gameService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllGames() {
        return gameService.getAllGames();
    }
}
