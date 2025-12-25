package com.vuckoapp.gamingservice.services;


import com.vuckoapp.gamingservice.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    public ResponseEntity<?> getAllGames() {
        return ResponseEntity.ok(gameRepository.findAll());
    }
}
