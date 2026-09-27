package com.sdlms.monitoring;

import com.sdlms.session.Session;
import com.sdlms.session.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;

@Service
public class MonitoringService {

    private static final int MIN_ENGAGED_MINUTES = 30;
    private static final int MIN_ENGAGED_WORDS = 50;

    private final SessionRepository sessionRepository;
    private final MonitoringEventRepository monitoringEventRepository;
    private final EngagementStatusRepository engagementStatusRepository;

    public MonitoringService(
            SessionRepository sessionRepository,
            MonitoringEventRepository monitoringEventRepository,
            EngagementStatusRepository engagementStatusRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.monitoringEventRepository = monitoringEventRepository;
        this.engagementStatusRepository = engagementStatusRepository;
    }

    /**
     * Adds this batch's counts to the session's running totals, then
     * re-evaluates the engagement rule (>=30 min session AND >=50 words typed).
     */
    @Transactional
    public void recordActivityBatch(ActivityBatchRequest batch) {

        Session session = sessionRepository.findById(batch.getSessionId())
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));

        MonitoringEvent event = monitoringEventRepository.findBySessionId(session.getId())
                .orElseGet(() -> MonitoringEvent.builder().session(session).build());

        event.setKeystrokeCount(event.getKeystrokeCount() + batch.getKeystrokeCount());
        event.setWordCount(event.getWordCount() + batch.getWordCount());
        event.setMouseClickCount(event.getMouseClickCount() + batch.getMouseClickCount());
        event.setMouseDistancePx(event.getMouseDistancePx() + batch.getMouseDistancePx());
        event.setCopyPasteCount(event.getCopyPasteCount() + batch.getCopyPasteCount());
        event.setIdleSeconds(event.getIdleSeconds() + batch.getIdleSeconds());
        event.setLastEventAt(OffsetDateTime.now());

        monitoringEventRepository.save(event);

        evaluateEngagement(session, event);
    }

    private void evaluateEngagement(Session session, MonitoringEvent event) {
        long sessionMinutes = Duration.between(session.getLoginTime(), OffsetDateTime.now()).toMinutes();

        boolean engaged = sessionMinutes >= MIN_ENGAGED_MINUTES && event.getWordCount() >= MIN_ENGAGED_WORDS;

        EngagementStatus status = engagementStatusRepository.findBySessionId(session.getId())
                .orElseGet(() -> EngagementStatus.builder().session(session).build());

        status.setIsEngaged(engaged);
        engagementStatusRepository.save(status);
    }
}
