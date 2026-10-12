# InternTrack

**Internship Application, Offer & Deadline Management System**

InternTrack is a full-stack web application that helps students organise their
internship search in one place — applications, offers and deadlines — while
giving administrators a platform-wide view of all activity.

---

## Tech stack

| Layer     | Technology                                                |
| --------- | --------------------------------------------------------- |
| Frontend  | React 18, Vite, React Router                              |
| Backend   | Spring Boot 3, Spring Security, Spring Data JPA, JWT      |
| Language  | Java 17                                                   |
| Database  | MySQL 8                                                   |
| Build     | Maven (backend), npm/Vite (frontend)                      |

## Features

**Students**
- Register / log in with JWT-based authentication.
- Add, edit and delete internship applications (company, role, location,
  work mode, stipend, applied date, deadline, source link, notes).
- Move each application through **Applied → Shortlisted → Selected / Rejected**.
- Record offers with an optional link to an internship, plus an offer letter
  link and a response deadline; **accept** or **decline** before it expires.
- A **deadline board** that colour-codes every application cut-off and offer
  response window as *overdue, due today, due soon or upcoming*.
- A personal dashboard with counts, a status pipeline and the next deadlines.

**Admin**
- Platform statistics (students, internships, offers, overdue deadlines).
- Full lists of students, internships and offers across all users.
- A global deadline board.

## Project structure

```
snake/
├── backend/            Spring Boot API (Java 17)
│   ├── Dockerfile
│   └── src/main/java/com/interntrack/
│       ├── config/      SecurityConfig, DataSeeder
│       ├── controller/  Auth, Internship, Offer, Deadline, Admin, Health
│       ├── dto/         request/response models
│       ├── exception/   ApiException + global handler
│       ├── model/       User, Internship, Offer, enums
│       ├── repository/  Spring Data JPA repositories
│       ├── security/    JWT filter, service, user details
│       └── service/     business logic
├── frontend/           React + Vite single-page app
│   └── src/{components,pages}
├── database/           interntrack_db.sql (reference schema)
├── render.yaml         Render backend deployment
├── start-backend.bat   Run the API locally
└── start-frontend.bat  Run the UI locally
```

## Running locally

Prerequisites: **Java 17+**, **Maven 3.9+**, **Node 18+**, **MySQL 8**.

1. **Database** — create an empty MySQL database (the app creates tables on
   first run):
   ```sql
   CREATE DATABASE interntrack_db;
   ```
   Default credentials used by the backend are `root` / `root`. Override them
   with the `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD`
   environment variables (or edit `backend/src/main/resources/application.properties`).

2. **Backend**
   ```bat
   start-backend.bat
   ```
   API runs at `http://localhost:8080` (health check: `/api/health`).

3. **Frontend**
   ```bat
   start-frontend.bat
   ```
   UI runs at `http://localhost:5173` and proxies `/api` to the backend.

## Demo accounts

Seeded automatically on first start:

| Role    | Email                          | Password     |
| ------- | ------------------------------ | ------------ |
| Admin   | admin@interntrack.com          | Admin@123    |
| Student | aarav.sharma@interntrack.dev   | Student@123  |
| Student | diya.nair@interntrack.dev      | Student@123  |
| Student | ishaan.verma@interntrack.dev   | Student@123  |

> Change the admin password and `JWT_SECRET` before deploying to production.

## API overview

| Method | Endpoint                        | Description                          |
| ------ | ------------------------------- | ------------------------------------ |
| POST   | `/api/auth/register`            | Register a student                   |
| POST   | `/api/auth/login`               | Log in, returns a JWT                |
| GET    | `/api/auth/me`                  | Current user                         |
| GET    | `/api/internships`              | List my internships (status/keyword) |
| POST   | `/api/internships`              | Add an internship                    |
| PUT    | `/api/internships/{id}`         | Update an internship                 |
| PATCH  | `/api/internships/{id}/status`  | Change application status            |
| DELETE | `/api/internships/{id}`         | Delete an internship                 |
| GET    | `/api/offers`                   | List my offers                       |
| POST   | `/api/offers`                   | Record an offer                      |
| PUT    | `/api/offers/{id}`              | Update an offer                      |
| POST   | `/api/offers/{id}/accept`       | Accept an offer                      |
| POST   | `/api/offers/{id}/decline`      | Decline an offer                     |
| DELETE | `/api/offers/{id}`              | Delete an offer                      |
| GET    | `/api/deadlines`                | My deadline board                    |
| GET    | `/api/admin/stats`              | Platform statistics (admin)          |
| GET    | `/api/admin/students`           | All students (admin)                 |
| GET    | `/api/admin/internships`        | All internships (admin)              |
| GET    | `/api/admin/offers`             | All offers (admin)                   |
| GET    | `/api/admin/deadlines`          | Global deadlines (admin)             |
| GET    | `/api/health`                   | Health check                         |

See [DEPLOYMENT.md](DEPLOYMENT.md) to publish the app, and
[PROJECT.md](PROJECT.md) for the abstract and system flow.
