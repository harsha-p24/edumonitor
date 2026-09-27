package com.sdlms.monitoring;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentActivityRow {
    private Long sessionId;
    private String studentName;
    private String usn;
    private String pcLabel;
    private long metricValue;   // meaning depends on which endpoint returned this row
    private String metricLabel; // e.g. "keystrokes", "words", "clicks", "idle_seconds"
}
