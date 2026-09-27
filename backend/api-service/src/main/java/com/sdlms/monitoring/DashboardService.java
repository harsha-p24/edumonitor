package com.sdlms.monitoring;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final MonitoringEventRepository monitoringEventRepository;

    public DashboardService(MonitoringEventRepository monitoringEventRepository) {
        this.monitoringEventRepository = monitoringEventRepository;
    }

    public List<StudentActivityRow> getKeyboardActivity(Long labId) {
        return monitoringEventRepository.findActiveByLabId(labId).stream()
                .map(e -> new StudentActivityRow(
                        e.getSession().getId(),
                        e.getSession().getStudent().getName(),
                        e.getSession().getStudent().getUsn(),
                        e.getSession().getLabPc().getPcLabel(),
                        e.getWordCount(),
                        "words_typed"
                ))
                .toList();
    }

    public List<StudentActivityRow> getMouseActivity(Long labId) {
        return monitoringEventRepository.findActiveByLabId(labId).stream()
                .map(e -> new StudentActivityRow(
                        e.getSession().getId(),
                        e.getSession().getStudent().getName(),
                        e.getSession().getStudent().getUsn(),
                        e.getSession().getLabPc().getPcLabel(),
                        e.getMouseClickCount(),
                        "mouse_clicks"
                ))
                .toList();
    }

    public List<StudentActivityRow> getCopyPasteActivity(Long labId) {
        return monitoringEventRepository.findActiveByLabId(labId).stream()
                .map(e -> new StudentActivityRow(
                        e.getSession().getId(),
                        e.getSession().getStudent().getName(),
                        e.getSession().getStudent().getUsn(),
                        e.getSession().getLabPc().getPcLabel(),
                        e.getCopyPasteCount(),
                        "copy_paste_events"
                ))
                .toList();
    }

    public List<StudentActivityRow> getIdleStudents(Long labId) {
        return monitoringEventRepository.findActiveByLabId(labId).stream()
                .map(e -> new StudentActivityRow(
                        e.getSession().getId(),
                        e.getSession().getStudent().getName(),
                        e.getSession().getStudent().getUsn(),
                        e.getSession().getLabPc().getPcLabel(),
                        e.getIdleSeconds(),
                        "idle_seconds"
                ))
                .toList();
    }
}
