package com.sdlms.session;

import com.sdlms.attendance.Attendance;
import com.sdlms.attendance.AttendanceRepository;
import com.sdlms.lab.LabPc;
import com.sdlms.lab.LabPcRepository;
import com.sdlms.timetable.Timetable;
import com.sdlms.timetable.TimetableRepository;
import com.sdlms.user.User;
import com.sdlms.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;
    private final LabPcRepository labPcRepository;
    private final TimetableRepository timetableRepository;
    private final UserRepository userRepository;

    public SessionService(
            SessionRepository sessionRepository,
            AttendanceRepository attendanceRepository,
            LabPcRepository labPcRepository,
            TimetableRepository timetableRepository,
            UserRepository userRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.attendanceRepository = attendanceRepository;
        this.labPcRepository = labPcRepository;
        this.timetableRepository = timetableRepository;
        this.userRepository = userRepository;
    }

    /**
     * Core flow: student logs in on a lab PC -> validate against the timetable
     * for right now -> create session -> mark attendance. All in one transaction:
     * either the whole thing succeeds, or none of it is saved.
     */
    @Transactional
    public SessionResponse login(String studentEmail, SessionLoginRequest request) {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        // Reject if this student already has an active session somewhere
        sessionRepository.findByStudentIdAndStatus(student.getId(), SessionStatus.ACTIVE)
                .ifPresent(s -> {
                    throw new IllegalStateException("Student already has an active session (id=" + s.getId() + ")");
                });

        LabPc labPc = labPcRepository.findById(request.getLabPcId())
                .orElseThrow(() -> new EntityNotFoundException("Lab PC not found"));

        // Convert Java's Monday=1..Sunday=7 (DayOfWeek.getValue()) - matches our schema exactly
        short dayOfWeek = (short) DayOfWeek.from(java.time.LocalDate.now()).getValue();
        LocalTime now = LocalTime.now();

        Timetable timetable = timetableRepository
                .findActiveSlot(labPc.getLab().getId(), dayOfWeek, now)
                .orElseThrow(() -> new IllegalStateException(
                        "No active timetable slot for this lab right now. Login rejected."));

        Session session = Session.builder()
                .student(student)
                .labPc(labPc)
                .timetable(timetable)
                .status(SessionStatus.ACTIVE)
                .build();
        session = sessionRepository.save(session);

        Attendance attendance = Attendance.builder()
                .session(session)
                .student(student)
                .build();
        attendanceRepository.save(attendance);

        return new SessionResponse(
                session.getId(),
                timetable.getSubject(),
                labPc.getLab().getName(),
                labPc.getPcLabel(),
                session.getLoginTime(),
                attendance.getStatus().name()
        );
    }
}
