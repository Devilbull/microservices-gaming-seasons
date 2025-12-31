package com.vuckoapp.gamingservice.repository;


import com.vuckoapp.gamingservice.model.SessionInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SessionInviteRepository extends JpaRepository<SessionInvite, UUID>, JpaSpecificationExecutor<SessionInvite> {
    Optional<SessionInvite> findByToken(String token);
}
