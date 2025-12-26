package com.vuckoapp.gamingservice.repository;

import com.vuckoapp.gamingservice.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface   GameRepository extends JpaRepository<Game, UUID> {
    Optional<Game> findByGameName(String gameName);

    boolean existsByGameName(String s);

    Page<Game> findByGameType(String gameType, Pageable pageable);

    Page<Game> findByGameNameContainingIgnoreCase(
            String gameName, Pageable pageable);

    Page<Game> findByGameTypeAndGameNameContainingIgnoreCase(
            String gameType, String gameName, Pageable pageable);

    @Transactional
    void deleteByGameName(String gameName);
}
