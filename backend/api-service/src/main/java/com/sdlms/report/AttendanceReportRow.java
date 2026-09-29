package com.sdlms.report;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class AttendanceReportRow {
    private String studentName;
    private String usn;
    private String subject;
    private String labName;
    private String status;
    private OffsetDateTime markedAt;
}
