-- ============================================================
-- EduMonitor Database Schema
-- PostgreSQL 16
-- ============================================================

-- Clean slate (safe for dev; remove in production migrations)
DROP TABLE IF EXISTS audit_log CASCADE;
DROP TABLE IF EXISTS warnings CASCADE;
DROP TABLE IF EXISTS engagement_status CASCADE;
DROP TABLE IF EXISTS monitoring_events CASCADE;
DROP TABLE IF EXISTS attendance CASCADE;
DROP TABLE IF EXISTS sessions CASCADE;
DROP TABLE IF EXISTS timetables CASCADE;
DROP TABLE IF EXISTS lab_pcs CASCADE;
DROP TABLE IF EXISTS labs CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================================
-- USERS  (Admin, Faculty, Student — single table, role-based)
-- ============================================================
CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(120)  NOT NULL,
    usn             VARCHAR(30)   UNIQUE,           -- NULL for admin/faculty, required for students
    email           VARCHAR(150)  NOT NULL UNIQUE,
    password_hash   VARCHAR(255)  NOT NULL,
    role            VARCHAR(20)   NOT NULL CHECK (role IN ('ADMIN', 'FACULTY', 'STUDENT')),
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ   NOT NULL DEFAULT now()
);

-- ============================================================
-- LABS  (Lab1, Lab2, Lab3...)
-- ============================================================
CREATE TABLE labs (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(60)   NOT NULL UNIQUE,   -- e.g. "Lab 1"
    location        VARCHAR(120),
    capacity        INT           NOT NULL DEFAULT 30,
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT now()
);

-- ============================================================
-- LAB PCs  (individual machines within a lab)
-- ============================================================
CREATE TABLE lab_pcs (
    id              BIGSERIAL PRIMARY KEY,
    lab_id          BIGINT        NOT NULL REFERENCES labs(id) ON DELETE CASCADE,
    pc_label        VARCHAR(30)   NOT NULL,          -- e.g. "PC-01"
    hostname        VARCHAR(120),
    is_active       BOOLEAN       NOT NULL DEFAULT TRUE,
    UNIQUE (lab_id, pc_label)
);

-- ============================================================
-- TIMETABLES  (which subject/faculty/lab is scheduled when)
-- ============================================================
CREATE TABLE timetables (
    id              BIGSERIAL PRIMARY KEY,
    lab_id          BIGINT        NOT NULL REFERENCES labs(id) ON DELETE CASCADE,
    faculty_id      BIGINT        NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    subject         VARCHAR(120)  NOT NULL,
    day_of_week     SMALLINT      NOT NULL CHECK (day_of_week BETWEEN 1 AND 7), -- 1=Mon ... 7=Sun
    start_time      TIME          NOT NULL,
    end_time        TIME          NOT NULL,
    batch           VARCHAR(60),                     -- e.g. "CSE-3A"
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT now(),
    CHECK (end_time > start_time)
);

-- ============================================================
-- SESSIONS  (the central table — created on valid login)
-- ============================================================
CREATE TABLE sessions (
    id              BIGSERIAL PRIMARY KEY,
    student_id      BIGINT        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    lab_pc_id       BIGINT        NOT NULL REFERENCES lab_pcs(id) ON DELETE RESTRICT,
    timetable_id    BIGINT        NOT NULL REFERENCES timetables(id) ON DELETE RESTRICT,
    login_time      TIMESTAMPTZ   NOT NULL DEFAULT now(),
    logout_time     TIMESTAMPTZ,
    status          VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'CLOSED')),
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT now()
);

-- ============================================================
-- ATTENDANCE  (marked the instant a valid session is created)
-- ============================================================
CREATE TABLE attendance (
    id              BIGSERIAL PRIMARY KEY,
    session_id      BIGINT        NOT NULL UNIQUE REFERENCES sessions(id) ON DELETE CASCADE,
    student_id      BIGINT        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status          VARCHAR(20)   NOT NULL DEFAULT 'PRESENT' CHECK (status IN ('PRESENT', 'ABSENT')),
    marked_at       TIMESTAMPTZ   NOT NULL DEFAULT now()
);

-- ============================================================
-- MONITORING EVENTS  (aggregated counts per session — NEVER raw content)
-- ============================================================
CREATE TABLE monitoring_events (
    id                  BIGSERIAL PRIMARY KEY,
    session_id          BIGINT      NOT NULL UNIQUE REFERENCES sessions(id) ON DELETE CASCADE,
    keystroke_count     BIGINT      NOT NULL DEFAULT 0,
    word_count          BIGINT      NOT NULL DEFAULT 0,
    mouse_click_count   BIGINT      NOT NULL DEFAULT 0,
    mouse_distance_px   BIGINT      NOT NULL DEFAULT 0,   -- cumulative movement
    copy_paste_count    BIGINT      NOT NULL DEFAULT 0,
    idle_seconds        BIGINT      NOT NULL DEFAULT 0,
    last_event_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================================================
-- ENGAGEMENT STATUS  (derived: 30-min + 50-word rule)
-- ============================================================
CREATE TABLE engagement_status (
    id              BIGSERIAL PRIMARY KEY,
    session_id      BIGINT        NOT NULL UNIQUE REFERENCES sessions(id) ON DELETE CASCADE,
    is_engaged      BOOLEAN       NOT NULL DEFAULT FALSE,
    evaluated_at    TIMESTAMPTZ   NOT NULL DEFAULT now()
);

-- ============================================================
-- WARNINGS  (faculty -> student nudges, fully logged)
-- ============================================================
CREATE TABLE warnings (
    id              BIGSERIAL PRIMARY KEY,
    session_id      BIGINT        NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    sender_id       BIGINT        NOT NULL REFERENCES users(id) ON DELETE RESTRICT,   -- faculty
    recipient_id    BIGINT        NOT NULL REFERENCES users(id) ON DELETE CASCADE,    -- student
    message         TEXT          NOT NULL,
    sent_at         TIMESTAMPTZ   NOT NULL DEFAULT now()
);

-- ============================================================
-- AUDIT LOG  (general system trail)
-- ============================================================
CREATE TABLE audit_log (
    id              BIGSERIAL PRIMARY KEY,
    actor_id        BIGINT        REFERENCES users(id) ON DELETE SET NULL,
    action          VARCHAR(120)  NOT NULL,
    details         TEXT,
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT now()
);
