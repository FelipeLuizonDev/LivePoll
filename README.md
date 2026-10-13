# LivePoll

Real-time polling system with a React administrative dashboard, an Android mobile application, and a REST API built with Java 21, Spring Boot, PostgreSQL, and Redis.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19-61DAFB.svg)](https://react.dev/)
[![Kotlin](https://img.shields.io/badge/Kotlin-Android-7F52FF.svg)](https://kotlinlang.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-red.svg)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-enabled-2496ED.svg)](https://www.docker.com/)

---

# Table of Contents

1. [About the Project](#about-the-project)
2. [Technologies](#technologies)
3. [Features](#features)
4. [Project Structure](#project-structure)
5. [Running the Project](#running-the-project)
6. [API Endpoints](#api-endpoints)
7. [Author](#author)

---

# About the Project

LivePoll is a real-time polling system developed to integrate Web and Mobile applications with a RESTful backend.

The system allows administrators to create and manage polls, control voting availability, and monitor results through a Web dashboard. Participants can access polls, submit votes, and view results through an Android application.

The backend follows a layered architecture with Controller, Service, Repository, and DTO components.

PostgreSQL is used for persistent data storage, while Redis supports vote counting and result retrieval.

---

# Technologies

- **Java 21**
- **Spring Boot 4.1.1**
- Spring Data JPA
- Spring Web MVC
- Bean Validation
- **PostgreSQL 17**
- **Redis 7**
- **React 19**
- TypeScript
- Vite
- Axios
- React Router
- **Kotlin**
- Android SDK
- Retrofit
- Maven
- Docker Compose

---

# Features

- Create polls with multiple options
- List all polls
- Retrieve poll details by ID
- Open and close polls
- Submit anonymous votes
- Retrieve poll results
- Display vote counts for each option
- Automatically refresh results on the Web dashboard
- Automatically refresh the poll list in the Android application
- Manually refresh polls on Android
- Validate poll titles and options
- Reject votes for closed polls
- Persist application data in PostgreSQL
- Use Redis for vote counting and result retrieval
- Provide API health-check endpoints

---

# Project Structure

```text
LivePoll/
│
├── backend/
│   └── src/main/java/com/livepoll/backend/
│       ├── config/
│       ├── controller/
│       ├── dto/
│       ├── entity/
│       ├── exception/
│       ├── repository/
│       └── service/
│
├── web/
│   └── src/
│       ├── components/
│       └── pages/
│
├── mobile/
│   └── app/src/main/
│
├── infrastructure/
│   └── docker-compose.yml
│
├── docs/
└── README.md
```

---

# Running the Project

## Prerequisites

- Java JDK 21
- Docker Desktop with Docker Compose
- Node.js and npm
- Android Studio
- Git

---

## Clone repository

```bash
git clone https://github.com/FelipeLuizonDev/LivePoll.git
cd LivePoll
```

---

## Start PostgreSQL and Redis

From the project root, run:

```bash
docker compose -f infrastructure/docker-compose.yml up -d
```

Check the containers:

```bash
docker compose -f infrastructure/docker-compose.yml ps
```

The development database configuration is:

| Setting | Value |
|---|---|
| Database | `livepoll` |
| Username | `livepoll` |
| Password | `livepoll_dev` |
| PostgreSQL port | `5432` |
| Redis port | `6379` |

These credentials are intended for local development only.

---

## Run the backend

Navigate to the backend directory:

```bash
cd backend
```

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**Linux/macOS:**

```bash
./mvnw spring-boot:run
```

The API runs at:

```text
http://localhost:8080
```

Verify the application health:

```bash
curl http://localhost:8080/api/v1/health
```

Expected response:

```json
{
  "application": "LivePoll API",
  "status": "UP"
}
```

---

## Run the Web application

Open a separate terminal from the project root:

```bash
cd web
npm install
npm run dev
```

Open the local URL displayed by Vite, usually:

```text
http://localhost:5173
```

To build the production version:

```bash
npm run build
```

To run the linter:

```bash
npm run lint
```

---

## Run the Android application

1. Open Android Studio.
2. Open the `mobile` directory.
3. Wait for Gradle synchronization to finish.
4. Start an Android emulator or connect a configured device.
5. Run the application from Android Studio.

The default Android emulator uses the following API base URL:

```text
http://10.0.2.2:8080/api/v1
```

For a physical device, configure the API base URL to use the host machine's reachable local network address.

---

## Run backend tests

From the `backend` directory:

**Windows:**

```powershell
.\mvnw.cmd test
```

**Linux/macOS:**

```bash
./mvnw test
```

---

# API Endpoints

Base URL:

```text
http://localhost:8080/api/v1
```

## Health Check

Check whether the API is running.

```http
GET /health
```

---

## Create Poll

Create a poll with a title and multiple options.

```http
POST /enquetes
Content-Type: application/json
```

Body:

```json
{
  "titulo": "Which programming language do you prefer?",
  "opcoes": [
    "Java",
    "Python",
    "JavaScript"
  ]
}
```

Validation rules:

- Title must contain between 3 and 200 characters.
- A poll must have between 2 and 10 options.
- Options cannot be blank.
- Each option can contain up to 100 characters.

A successful request returns `201 Created`.

---

## List Polls

Retrieve all polls.

```http
GET /enquetes
```

---

## Get Poll by ID

Retrieve a specific poll and its options.

```http
GET /enquetes/{id}
```

---

## Open Poll

Enable voting for a poll.

```http
PATCH /enquetes/{id}/abrir
```

---

## Close Poll

Close a poll and prevent further voting.

```http
PATCH /enquetes/{id}/fechar
```

---

## Submit Vote

Submit an anonymous vote for an option.

```http
POST /enquetes/{id}/votos
Content-Type: application/json
```

Body:

```json
{
  "opcaoId": 1
}
```

A successful request returns `200 OK` without a response body.

Voting is only accepted when the poll is open and the submitted option is valid.

---

## Get Poll Results

Retrieve the total number of votes for each option.

```http
GET /enquetes/{id}/resultados
```

Example response:

```json
{
  "enqueteId": 1,
  "titulo": "Which programming language do you prefer?",
  "aberta": true,
  "opcoes": [
    {
      "opcaoId": 1,
      "textoOpcao": "Java",
      "votos": 3
    },
    {
      "opcaoId": 2,
      "textoOpcao": "Python",
      "votos": 2
    }
  ]
}
```

The values above are illustrative; actual results depend on the votes stored in the system.

---

# Author

**Felipe Luizon**

- GitHub: [FelipeLuizonDev](https://github.com/FelipeLuizonDev)
- LinkedIn: [felipeluizon](https://linkedin.com/in/felipeluizon)
