# Login & Register App with To-Do List and Chat

A simple, beginner-friendly Java web application built with **Spring Boot**.
Users can register an account, log in, manage a personal to-do list,
chat with other logged-in users, and log out.

The code is intentionally minimal — no Spring Security filter chain, no JWT,
no WebSockets, no roles, no REST APIs in the formal sense. Just plain
Spring MVC, JPA and Thymeleaf so a beginner can follow the entire request flow
end to end.

---

## Features

- User registration with server-side validation
- Passwords hashed with **BCrypt** (never stored in plain text)
- Login / logout using an HTTP session
- Protected `/home` and `/chat` pages (unauthenticated users are redirected to login)
- Personal **To-Do list** per user (add, mark complete, delete)
- Shared **chat room** — every logged-in user can post and read messages
- Tasks and chat operations are always scoped to the logged-in session
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
| Chat updates | Browser polling every 2 seconds (no WebSockets) |
| Build tool | Maven |

---

## Project Structure
login-register/
├── src/main/java/com/example/loginregister/
│ ├── LoginRegisterApplication.java
│ ├── controller/
│ │ ├── AuthController.java
│ │ ├── HomeController.java
│ │ └── ChatController.java
│ ├── model/
│ │ ├── User.java
│ │ ├── Todo.java
│ │ └── ChatMessage.java
│ ├── repository/
│ │ ├── UserRepository.java
│ │ ├── TodoRepository.java
│ │ └── ChatMessageRepository.java
│ └── service/
│ ├── UserService.java
│ ├── TodoService.java
│ └── ChatService.java
│
├── src/main/resources/
│ ├── templates/
│ │ ├── login.html
│ │ ├── register.html
│ │ ├── home.html
│ │ └── chat.html
│ ├── static/css/style.css
│ └── application.properties
│
├── pom.xml
└── README.md
