package com.sdlms.monitoring;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityBatchRequest {

    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @Min(value = 0, message = "Keystroke count cannot be negative")
    private long keystrokeCount;

    @Min(value = 0, message = "Word count cannot be negative")
    private long wordCount;

    @Min(value = 0, message = "Mouse click count cannot be negative")
    private long mouseClickCount;

    @Min(value = 0, message = "Mouse distance cannot be negative")
    private long mouseDistancePx;

    @Min(value = 0, message = "Copy-paste count cannot be negative")
    private long copyPasteCount;

    @Min(value = 0, message = "Idle seconds cannot be negative")
    private long idleSeconds;
}
