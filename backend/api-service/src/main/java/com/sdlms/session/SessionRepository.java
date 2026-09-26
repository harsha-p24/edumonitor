package com.sdlms.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByStudentId(Long studentId);

    List<Session> findByLabPcId(Long labPcId);

    List<Session> findByStatus(SessionStatus status);

    /**
     * A student can only have one ACTIVE session at a time.
     * Used to prevent duplicate/overlapping logins.
     */
    Optional<Session> findByStudentIdAndStatus(Long studentId, SessionStatus status);
}
