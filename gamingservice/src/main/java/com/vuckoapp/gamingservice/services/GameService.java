package com.vuckoapp.gamingservice.services;


import com.vuckoapp.gamingservice.dto.GameDto;
import com.vuckoapp.gamingservice.model.Game;
import com.vuckoapp.gamingservice.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;



    public Page<GameDto> getAllGames(
            String gameType,
            String gameName,
            Pageable pageable
    ) {
        Page<Game> page;

        if (gameType != null && gameName != null) {
            page = gameRepository
                    .findByGameTypeIgnoreCaseAndGameNameContainingIgnoreCase(
                            gameType, gameName, pageable);
        } else if (gameType != null) {
            page = gameRepository
                    .findByGameTypeContainingIgnoreCase(gameType, pageable);
        } else if (gameName != null) {
            page = gameRepository
                    .findByGameNameContainingIgnoreCase(gameName, pageable);
        } else {
            page = gameRepository.findAll(pageable);
        }

        return page.map(gameMapper::toDto);
    }

    public GameDto getGameByName(String name){
        Game game = gameRepository.findByGameName(name)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        return gameMapper.toDto(game);
    }

    public GameDto addGame(GameDto dto) {
        if(gameRepository.existsByGameName(dto.gameName())){
            throw new RuntimeException("Game already exists");
        }
        if(dto.gameName() == null || dto.gameDescription() == null || dto.gameType() == null){
            throw new RuntimeException("Did not provide all required fields");
        }
        Game game = gameMapper.toEntity(dto);

        gameRepository.save(game);
        return gameMapper.toDto(game);
    }

    @Transactional
    public void deleteGameByName(String name){
        if(!gameRepository.existsByGameName(name)){
            throw new RuntimeException("Game does not exists");
        }
        Game game =  gameRepository.findByGameName(name)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        gameRepository.delete(game);

    }

    @Transactional
    public void deleteAllGames(){
        gameRepository.deleteAll();
    }
}
