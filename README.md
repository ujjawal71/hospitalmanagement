# Online Doctor Appointment Booking System

Java Spring Boot backend with MySQL and HTML/CSS/JavaScript frontend (served from the same project).

## Technology Stack

- **Backend:** Java 17, Spring Boot 3.2, Spring Data JPA, Spring Security
- **Database:** MySQL
- **Frontend:** HTML, CSS, JavaScript (static resources under `src/main/resources/static`)

## Prerequisites

- JDK 17+
- Maven 3.6+
- MySQL 8 (or compatible)

## Database Setup

1. Create a database (optional; app can create it if allowed):
   ```sql
   CREATE DATABASE IF NOT EXISTS hospital_db;
   ```
2. Update `src/main/resources/application.properties` with your MySQL username and password:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

## How to Run

```bash
mvn spring-boot:run
```

- Backend and frontend run together on **http://localhost:8080**
- Open **http://localhost:8080** or **http://localhost:8080/index.html** in a browser.

## Default Login (after first run)

| Role   | Email               | Password   |
|--------|---------------------|------------|
| Admin  | admin@hospital.com  | admin123   |
| Doctor | doctor@hospital.com | doctor123  |
| Patient| (register from UI)   | (your pwd) |

## Features

### Admin
- Login
- Add / Update / Delete doctors
- Manage departments (Cardiology, Dental, etc.)
- View all appointments
- Manage users (patients and doctors)

### Doctor
- Login
- View assigned appointments
- Update appointment status (Approved / Completed / Cancelled)
- View patient details
- Set available time slots

### Patient
- Registration and login
- Search doctor by department
- Book appointment
- View appointment status
- Cancel appointment
- Update profile

## API Overview

- **Auth:** `POST /api/auth/login`, `POST /api/auth/register`, `POST /api/auth/logout`
- **Admin:** `/api/admin/departments`, `/api/admin/doctors`, `/api/admin/patients`, `/api/admin/appointments`
- **Doctor:** `/api/doctor/appointments`, `/api/doctor/appointments/{id}/status`, `/api/doctor/appointments/{id}/patient`, `/api/doctor/time-slots`
- **Patient:** `/api/patient/departments`, `/api/patient/doctors/by-department/{id}`, `/api/patient/appointments`, `/api/patient/profile`
- **Public:** `GET /api/departments/list`, `GET /api/doctors/by-department?departmentId=`

All authenticated APIs require header: `X-Auth-Token: <token>` (returned on login).

## Project Structure

```
src/main/java/com/hospital/
├── config/          # Security, Web, Data init
├── controller/      # REST: Auth, Admin, Doctor, Patient, Public
├── dto/             # DTOs
├── entity/          # JPA entities
├── repository/      # JPA repositories
└── service/         # Business logic
src/main/resources/
├── application.properties
└── static/
    ├── index.html
    ├── css/style.css
    ├── js/app.js
    └── pages/       # Admin, Doctor, Patient HTML pages
```
