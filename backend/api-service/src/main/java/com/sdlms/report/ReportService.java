package com.sdlms.report;

import com.sdlms.attendance.Attendance;
import com.sdlms.attendance.AttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class ReportService {

    private final AttendanceRepository attendanceRepository;

    public ReportService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Transactional(readOnly = true)
    public List<AttendanceReportRow> getDailyReport(LocalDate date, Long labId) {
        OffsetDateTime from = date.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime to = date.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC);
        return fetchAndMap(from, to, labId);
    }

    @Transactional(readOnly = true)
    public List<AttendanceReportRow> getWeeklyReport(LocalDate anyDateInWeek, Long labId) {
        LocalDate weekStart = anyDateInWeek.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate weekEnd = weekStart.plusDays(7);
        OffsetDateTime from = weekStart.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime to = weekEnd.atStartOfDay().atOffset(ZoneOffset.UTC);
        return fetchAndMap(from, to, labId);
    }

    @Transactional(readOnly = true)
    public List<AttendanceReportRow> getMonthlyReport(int year, int month, Long labId) {
        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = monthStart.plusMonths(1);
        OffsetDateTime from = monthStart.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime to = monthEnd.atStartOfDay().atOffset(ZoneOffset.UTC);
        return fetchAndMap(from, to, labId);
    }

    @Transactional(readOnly = true)
    public List<AttendanceReportRow> getOverallReport(Long labId) {
        OffsetDateTime from = OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime to = OffsetDateTime.now().plusYears(1); // safely covers "everything so far"
        return fetchAndMap(from, to, labId);
    }

    private List<AttendanceReportRow> fetchAndMap(OffsetDateTime from, OffsetDateTime to, Long labId) {
        List<Attendance> records = attendanceRepository.findByDateRange(from, to, labId);
        return records.stream()
                .map(a -> new AttendanceReportRow(
                        a.getStudent().getName(),
                        a.getStudent().getUsn(),
                        a.getSession().getTimetable().getSubject(),
                        a.getSession().getLabPc().getLab().getName(),
                        a.getStatus().name(),
                        a.getMarkedAt()
                ))
                .toList();
    }
}
