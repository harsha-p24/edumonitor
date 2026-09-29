package com.sdlms.notification;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warnings")
public class WarningController {

    private final WarningService warningService;

    public WarningController(WarningService warningService) {
        this.warningService = warningService;
    }

    @PostMapping
    public ResponseEntity<WarningResponse> sendWarning(
            @Valid @RequestBody WarningRequest request,
            Authentication authentication
    ) {
        String facultyEmail = authentication.getName();
        WarningResponse response = warningService.sendWarning(facultyEmail, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{studentId}")
    public List<WarningResponse> getWarningHistory(@PathVariable Long studentId) {
        return warningService.getWarningHistoryForStudent(studentId);
    }
}
