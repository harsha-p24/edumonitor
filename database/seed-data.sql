-- ============================================================
-- Seed data for development/testing
-- Password hash below is a bcrypt hash of "password123" (placeholder — regenerate later)
-- ============================================================

-- Admin
INSERT INTO users (name, usn, email, password_hash, role) VALUES
('System Admin', NULL, 'admin@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'ADMIN');

-- Faculty (8)
INSERT INTO users (name, usn, email, password_hash, role) VALUES
('Shruthi M G', NULL, 'shruthi.mg@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'FACULTY'),
('Faculty Two', NULL, 'faculty2@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'FACULTY'),
('Faculty Three', NULL, 'faculty3@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'FACULTY'),
('Faculty Four', NULL, 'faculty4@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'FACULTY'),
('Faculty Five', NULL, 'faculty5@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'FACULTY'),
('Faculty Six', NULL, 'faculty6@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'FACULTY'),
('Faculty Seven', NULL, 'faculty7@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'FACULTY'),
('Faculty Eight', NULL, 'faculty8@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'FACULTY');

-- Students (sample batch of 10)
INSERT INTO users (name, usn, email, password_hash, role) VALUES
('Harshavardhana D', '1RF25MC040', 'harsha@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'STUDENT'),
('Student Two', '1RF25MC002', 'student2@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'STUDENT'),
('Student Three', '1RF25MC003', 'student3@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'STUDENT'),
('Student Four', '1RF25MC004', 'student4@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'STUDENT'),
('Student Five', '1RF25MC005', 'student5@edumonitor.local', '$2a$10$abcdefghijklmnopqrstuv', 'STUDENT');

-- Labs (4)
INSERT INTO labs (name, location, capacity) VALUES
('Lab 1', 'Block A, 2nd Floor', 30),
('Lab 2', 'Block A, 2nd Floor', 30),
('Lab 3', 'Block B, 1st Floor', 30),
('Lab 4', 'Block B, 1st Floor', 30);

-- PCs for Lab 1 (5 sample PCs)
INSERT INTO lab_pcs (lab_id, pc_label, hostname) VALUES
(1, 'PC-01', 'LAB1-PC01'),
(1, 'PC-02', 'LAB1-PC02'),
(1, 'PC-03', 'LAB1-PC03'),
(1, 'PC-04', 'LAB1-PC04'),
(1, 'PC-05', 'LAB1-PC05');

-- Sample timetable slot: Faculty "Shruthi M G" teaches in Lab 1, Monday, 9-11am
INSERT INTO timetables (lab_id, faculty_id, subject, day_of_week, start_time, end_time, batch) VALUES
(1, 2, 'Data Structures Lab', 1, '09:00', '11:00', 'CSE-3A');
