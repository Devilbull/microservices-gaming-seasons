package com.vuckoapp.gamingservice.repository;

import com.vuckoapp.gamingservice.model.Session;
import com.vuckoapp.gamingservice.model.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


public interface  SessionRepository extends JpaRepository<Session, UUID>, JpaSpecificationExecutor<Session> {

    boolean existsBySessionName(String s);

    @Query("""
    SELECT s FROM Session s
    WHERE s.sessionStatus = 'SCHEDULED'
      AND s.startOfSession BETWEEN :from AND :to
      AND s.reminder60Sent = false
""")
    List<Session> findSessionsStartingBetween(
            LocalDateTime from,
            LocalDateTime to
    );

    Page<Session> findAllByCreatorIdAndSessionStatus(
            UUID creatorId,
            SessionStatus status,
            Pageable pageable
    );
}
