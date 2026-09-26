package com.sdlms.session;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class SessionResponse {
    private Long sessionId;
    private String subject;
    private String labName;
    private String pcLabel;
    private OffsetDateTime loginTime;
    private String attendanceStatus;
}
