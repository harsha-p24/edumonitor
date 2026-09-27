package com.sdlms.monitoring;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonitoringEventRepository extends JpaRepository<MonitoringEvent, Long> {
    Optional<MonitoringEvent> findBySessionId(Long sessionId);
}
