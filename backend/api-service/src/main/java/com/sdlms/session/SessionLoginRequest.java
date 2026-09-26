package com.sdlms.session;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionLoginRequest {

    @NotNull(message = "Lab PC ID is required")
    private Long labPcId;
}
