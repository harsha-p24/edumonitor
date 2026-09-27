package com.sdlms.monitoring;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    private final MonitoringService monitoringService;

    public MonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @PostMapping("/activity")
    public ResponseEntity<Void> recordActivity(@Valid @RequestBody ActivityBatchRequest batch) {
        monitoringService.recordActivityBatch(batch);
        return ResponseEntity.noContent().build();
    }
}
