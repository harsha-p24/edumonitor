-- ============================================================
-- Indexes for common query patterns
-- ============================================================

-- Find active sessions for a lab quickly
CREATE INDEX idx_sessions_lab_pc ON sessions(lab_pc_id);
CREATE INDEX idx_sessions_student ON sessions(student_id);
CREATE INDEX idx_sessions_timetable ON sessions(timetable_id);
CREATE INDEX idx_sessions_status ON sessions(status);

-- Attendance reports by date range
CREATE INDEX idx_attendance_marked_at ON attendance(marked_at);
CREATE INDEX idx_attendance_student ON attendance(student_id);

-- Monitoring dashboard buttons (keyboard/mouse/copy-paste/idle views)
CREATE INDEX idx_monitoring_session ON monitoring_events(session_id);
CREATE INDEX idx_monitoring_updated_at ON monitoring_events(updated_at);

-- Engagement lookups
CREATE INDEX idx_engagement_session ON engagement_status(session_id);
CREATE INDEX idx_engagement_is_engaged ON engagement_status(is_engaged);

-- Warnings history
CREATE INDEX idx_warnings_recipient ON warnings(recipient_id);
CREATE INDEX idx_warnings_session ON warnings(session_id);

-- Timetable lookups for login validation (lab + day + time window)
CREATE INDEX idx_timetables_lab_day ON timetables(lab_id, day_of_week);

-- Audit log by actor and time
CREATE INDEX idx_audit_actor ON audit_log(actor_id);
CREATE INDEX idx_audit_created_at ON audit_log(created_at);
