# EduMonitor — Digital Lab Attendance and Activity Monitor

Role-based web application for automated lab attendance and activity-based
engagement tracking, built with React, Spring Boot, PostgreSQL, Redis and RabbitMQ.

## Status
🚧 Under active development — built step by step, phase by phase.

## Architecture
- `frontend/` — React app (Admin/Faculty/Student dashboards)
- `backend/api-service/` — Spring Boot REST + WebSocket API
- `backend/monitoring-worker/` — Queue consumer, batches writes to PostgreSQL
- `monitoring-agent/` — Windows .NET Worker Service (lab PC agent)
- `database/` — Schema, seed data, indexes
- `infra/` — Redis, queue, load balancer configs
- `docker/` — Dockerfiles + docker-compose
- `documentation/` — ER diagram, API contract, privacy policy, build plan

See `documentation/build-plan.md` for the phase-by-phase build log.
