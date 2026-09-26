package com.sdlms.session;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<SessionResponse> login(
            @Valid @RequestBody SessionLoginRequest request,
            Authentication authentication
    ) {
        String studentEmail = authentication.getName();
        SessionResponse response = sessionService.login(studentEmail, request);
        return ResponseEntity.ok(response);
    }
}
