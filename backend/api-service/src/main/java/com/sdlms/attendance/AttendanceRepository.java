package com.sdlms.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findBySessionId(Long sessionId);
    List<Attendance> findByStudentId(Long studentId);
}
