package com.sdlms.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarningRepository extends JpaRepository<Warning, Long> {
    List<Warning> findByRecipientIdOrderBySentAtDesc(Long recipientId);
    List<Warning> findBySessionIdOrderBySentAtDesc(Long sessionId);
    List<Warning> findBySenderIdOrderBySentAtDesc(Long senderId);
}
