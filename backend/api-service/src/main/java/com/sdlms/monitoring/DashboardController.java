package com.sdlms.monitoring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard/lab/{labId}")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/keyboard-activity")
    public List<StudentActivityRow> keyboardActivity(@PathVariable Long labId) {
        return dashboardService.getKeyboardActivity(labId);
    }

    @GetMapping("/mouse-activity")
    public List<StudentActivityRow> mouseActivity(@PathVariable Long labId) {
        return dashboardService.getMouseActivity(labId);
    }

    @GetMapping("/copy-paste-activity")
    public List<StudentActivityRow> copyPasteActivity(@PathVariable Long labId) {
        return dashboardService.getCopyPasteActivity(labId);
    }

    @GetMapping("/idle-students")
    public List<StudentActivityRow> idleStudents(@PathVariable Long labId) {
        return dashboardService.getIdleStudents(labId);
    }
}
