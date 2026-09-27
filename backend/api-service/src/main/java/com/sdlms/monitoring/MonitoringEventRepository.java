package com.sdlms.monitoring;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MonitoringEventRepository extends JpaRepository<MonitoringEvent, Long> {

    Optional<MonitoringEvent> findBySessionId(Long sessionId);

    /**
     * All monitoring events for currently ACTIVE sessions in a given lab.
     * Powers all four Teacher Dashboard activity buttons.
     */
    @Query("""
            SELECT e FROM MonitoringEvent e
            JOIN e.session s
            JOIN s.labPc pc
            WHERE pc.lab.id = :labId
              AND s.status = com.sdlms.session.SessionStatus.ACTIVE
            """)
    List<MonitoringEvent> findActiveByLabId(@Param("labId") Long labId);
}
