package com.sdlms.timetable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    List<Timetable> findByLabId(Long labId);

    List<Timetable> findByFacultyId(Long facultyId);

    /**
     * Finds the timetable slot (if any) that covers the given lab, day of week,
     * and a specific point in time. Used to validate a student login against
     * the schedule before creating a session.
     */
    @Query("""
            SELECT t FROM Timetable t
            WHERE t.lab.id = :labId
              AND t.dayOfWeek = :dayOfWeek
              AND :currentTime BETWEEN t.startTime AND t.endTime
            """)
    Optional<Timetable> findActiveSlot(
            @Param("labId") Long labId,
            @Param("dayOfWeek") Short dayOfWeek,
            @Param("currentTime") LocalTime currentTime
    );
}
