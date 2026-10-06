# Login & Register App with To-Do List

A simple, beginner-friendly Java web application built with **Spring Boot**.
Users can register an account, log in, manage a personal to-do list, and log out.

The code is intentionally minimal — no Spring Security filter chain, no JWT,
no roles, no REST APIs. Just plain Spring MVC, JPA and Thymeleaf so a beginner
can follow the entire request flow.

---

## Features

- User registration with validation
- Passwords hashed with **BCrypt** (never stored in plain text)
- Login / logout using an HTTP session
- Protected `/home` page (unauthenticated users are redirected to login)
- Personal **To-Do list** per user (add, mark complete, delete)
- Tasks are scoped to the logged-in user — you cannot see or modify another user's tasks
- MySQL tables created automatically by Hibernate (`ddl-auto=update`)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5 |
| Web | Spring MVC |
| Persistence | Spring Data JPA (Hibernate) |
| Database | MySQL 8 |
| Templates | Thymeleaf |
| Password hashing | `spring-security-crypto` (BCrypt only) |
| Build tool | Maven |

---

## Project Structure
