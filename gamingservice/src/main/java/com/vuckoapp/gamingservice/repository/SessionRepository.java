package com.vuckoapp.gamingservice.repository;

import com.vuckoapp.gamingservice.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface  SessionRepository extends JpaRepository<Session, UUID> {

    boolean existsBySessionName(String s);
}
