package com.sdlms.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findBySessionId(Long sessionId);

    List<Attendance> findByStudentId(Long studentId);

    /**
     * Attendance records within a date range, optionally filtered by lab.
     * Powers the daily/weekly/monthly/overall report views.
     */
    @Query("""
            SELECT a FROM Attendance a
            JOIN a.session s
            JOIN s.labPc pc
            WHERE a.markedAt BETWEEN :from AND :to
              AND (:labId IS NULL OR pc.lab.id = :labId)
            ORDER BY a.markedAt DESC
            """)
    List<Attendance> findByDateRange(
            @Param("from") OffsetDateTime from,
            @Param("to") OffsetDateTime to,
            @Param("labId") Long labId
    );
}
