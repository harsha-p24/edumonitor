package com.sdlms.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class WarningResponse {
    private Long id;
    private String senderName;
    private String recipientName;
    private String recipientUsn;
    private String message;
    private OffsetDateTime sentAt;
}
