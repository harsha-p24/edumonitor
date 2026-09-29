package com.sdlms.notification;

import com.sdlms.session.Session;
import com.sdlms.session.SessionRepository;
import com.sdlms.user.User;
import com.sdlms.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarningService {

    private final WarningRepository warningRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public WarningService(
            WarningRepository warningRepository,
            SessionRepository sessionRepository,
            UserRepository userRepository
    ) {
        this.warningRepository = warningRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public WarningResponse sendWarning(String facultyEmail, WarningRequest request) {

        User faculty = userRepository.findByEmail(facultyEmail)
                .orElseThrow(() -> new EntityNotFoundException("Faculty not found"));

        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new EntityNotFoundException("Session not found"));

        Warning warning = Warning.builder()
                .session(session)
                .sender(faculty)
                .recipient(session.getStudent())
                .message(request.getMessage())
                .build();

        warning = warningRepository.save(warning);

        // TODO (Phase 8/WebSocket): push this warning live to the student's agent
        // for an on-screen popup. For now, it's recorded and retrievable via API only.

        return new WarningResponse(
                warning.getId(),
                faculty.getName(),
                session.getStudent().getName(),
                session.getStudent().getUsn(),
                warning.getMessage(),
                warning.getSentAt()
        );
    }

    @Transactional(readOnly = true)
    public List<WarningResponse> getWarningHistoryForStudent(Long studentId) {
        return warningRepository.findByRecipientIdOrderBySentAtDesc(studentId).stream()
                .map(w -> new WarningResponse(
                        w.getId(),
                        w.getSender().getName(),
                        w.getRecipient().getName(),
                        w.getRecipient().getUsn(),
                        w.getMessage(),
                        w.getSentAt()
                ))
                .toList();
    }
}
