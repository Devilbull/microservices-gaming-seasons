package com.vuckoapp.userservice.repository;

import com.vuckoapp.userservice.model.GamerStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GamerStatsRepository extends JpaRepository<GamerStats, UUID> {
}
