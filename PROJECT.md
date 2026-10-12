# InternTrack — Abstract & System Flow

## Abstract

Students applying for internships typically track their progress across
spreadsheets, chat messages and memory. Applications get lost, offer response
deadlines are missed, and there is no single view of what stage each
application is in. **InternTrack** solves this by giving each student a
dedicated workspace to record internship applications, follow them through the
hiring pipeline, capture offers, and monitor every important date on one
deadline board.

An administrator role provides a consolidated view of all students,
applications, offers and upcoming deadlines for monitoring and reporting.

## Objectives

1. Provide secure, per-student management of internship applications.
2. Track each application's status: **Applied, Shortlisted, Selected, Rejected**.
3. Record internship offers with response deadlines and accept/decline them.
4. Surface all application and offer deadlines with clear urgency levels.
5. Give administrators platform-wide visibility and statistics.

## Backend flow

1. **Authentication**
   - A student registers or logs in at `/api/auth/*`.
   - The server verifies credentials with Spring Security and BCrypt, then
     issues a signed **JWT**.
   - Every subsequent request carries `Authorization: Bearer <token>`, which the
     `JwtAuthFilter` validates before the request reaches a controller.

2. **Internship management (student-owned)**
   - A student creates internships; each record references the owning user via
     `user_id`.
   - All queries are scoped to the authenticated user, so a student can only see
     and modify their own records.
   - Status transitions are applied through
     `PATCH /api/internships/{id}/status`.

3. **Offer management**
   - A student records an offer, optionally linking it to one of their
     internships.
   - Linking an offer automatically moves that internship to **Selected**.
   - Offers are **Pending** until accepted or declined; past their response
     deadline they are automatically marked **Expired**.

4. **Deadline reminders**
   - The deadline board merges internship deadlines and pending offer response
     windows into a single list.
   - Each item is classified as **OVERDUE, TODAY, SOON** (configurable window)
   **or UPCOMING**.

5. **Administration**
   - `/api/admin/**` is restricted to `ROLE_ADMIN`.
   - Endpoints expose aggregated statistics and read-only lists across all
     students.

## Data model

- **User** — id, name, email (unique), password (BCrypt), role, profile fields.
- **Internship** — belongs to a user; company, role, location, work mode,
  stipend, application date, deadline, status, source link, notes.
- **Offer** — belongs to a user; optional link to an internship, company, role,
  stipend, offer/joining dates, response deadline, status, letter link, notes.

## Technologies

React, Vite, Spring Boot, Java 17, Spring Security, JWT, Spring Data JPA, MySQL.

## Non-functional notes

- Stateless JWT authentication; passwords hashed with BCrypt.
- CORS enabled for the SPA; role-based authorization on admin routes.
- Centralised exception handling returns consistent JSON error payloads.
