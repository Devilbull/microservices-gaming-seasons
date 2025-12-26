package com.vuckoapp.gamingservice.controllers;

import com.vuckoapp.gamingservice.dto.GameDeleteDto;
import com.vuckoapp.gamingservice.dto.GameDto;
import com.vuckoapp.gamingservice.model.Game;
import com.vuckoapp.gamingservice.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;


    @GetMapping("/all")
    public List<GameDto> getAllGames() {
        return gameService.getAllGames();
    }

    //@PreAuthorize("isAuthenticated()")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-game")
    public ResponseEntity<?> addGame(@RequestBody GameDto dto) {
        gameService.addGame(dto);
        return ResponseEntity.ok(Map.of("message", "Game added successfully"));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete-game")
    public ResponseEntity<?> deleteGame(@RequestBody GameDeleteDto dto) {
        gameService.deleteGameByName(dto.gameName());
        return ResponseEntity.ok(Map.of("message", "Game deleted successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete-all-games")
    public ResponseEntity<?> deleteAllGame() {
        gameService.deleteAllGames();
        return ResponseEntity.ok(Map.of("message", "Games deleted successfully"));
    }

}
