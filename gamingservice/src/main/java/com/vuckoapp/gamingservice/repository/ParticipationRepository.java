package com.vuckoapp.gamingservice.repository;

import com.vuckoapp.gamingservice.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface ParticipationRepository extends JpaRepository< Participation,UUID> {

    boolean existsByUserIdAndSessionId(UUID userID, UUID sessionID);

    ArrayList<Participation> findAllBySessionId(UUID sessionID);
}
