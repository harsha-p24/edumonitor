package com.sdlms.monitoring;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EngagementStatusRepository extends JpaRepository<EngagementStatus, Long> {
    Optional<EngagementStatus> findBySessionId(Long sessionId);
}
