package com.sdlms.report;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports/attendance")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/daily")
    public List<AttendanceReportRow> daily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long labId
    ) {
        return reportService.getDailyReport(date, labId);
    }

    @GetMapping("/weekly")
    public List<AttendanceReportRow> weekly(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long labId
    ) {
        return reportService.getWeeklyReport(date, labId);
    }

    @GetMapping("/monthly")
    public List<AttendanceReportRow> monthly(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) Long labId
    ) {
        return reportService.getMonthlyReport(year, month, labId);
    }

    @GetMapping("/overall")
    public List<AttendanceReportRow> overall(@RequestParam(required = false) Long labId) {
        return reportService.getOverallReport(labId);
    }
}
