# corebank-monolith

**corebank-monolith**  
**Phase 1 – Legacy Monolith**

**Document Version:** 1.1  
**Last Updated:** May 04, 2026  
**Status:** Active  
**Project Series:** CoreBank Modernization Journey

---

### 1. Executive Summary

The `corebank-monolith` is a single **Spring Boot 4.0.6** Java 21 application that simulates a realistic **legacy banking backend** as it exists in many financial institutions today (including technical debt patterns seen in production at Davivienda and similar Colombian/Canadian banks).

It implements two core banking domains in one tightly coupled application:
- **Authentication** (login + JWT + custom banking headers)
- **Homepage / Product Aggregation** (retrieval of accounts, cards, balances)

**Purpose of Phase 1**: Establish a deliberately imperfect starting point that reflects real-world legacy constraints (mixed concerns, blocking I/O, duplicated logic, hard-to-scale architecture). This monolith will be migrated in later phases to clean microservices using Hexagonal Architecture, DDD, SOLID, and reactive patterns.

**Key Characteristics**
- Classic layered architecture (Controller → Service → Repository) with intentional technical debt
- Synchronous blocking operations (Spring MVC)
- Custom banking-grade security headers
- Standardized `ResponseDTO` wrapper
- Minimum **80%** unit test coverage (JaCoCo enforced)
- Uses current industry tooling (Spring Boot 4.0.6 + Java 21 + Gradle Kotlin DSL)
- Ready for Docker Compose deployment

This phase serves as the baseline for the entire modernization journey.

---

### 2. System Overview

**Purpose**  
Provide a functional but legacy-style backend that handles client authentication and returns an aggregated homepage view of banking products — exactly as required for later modernization phases.

**Business Capabilities**
- Authenticate a client and issue a JWT with custom headers
- Return aggregated product data (accounts, cards, balances) for an authenticated client
- Simulate real banking header propagation and response standardization

**Non-Functional Goals**
- Minimum 80% unit test coverage
- Production-like logging and observability
- Easy to fork and migrate in Phase 2

---

### 3. Architecture

**Architecture Style**  
Classic **Layered Architecture** (intentionally not clean) — this is the “before” picture of the modernization journey.

```
corebank-monolith/
├── src/main/kotlin/
│   ├── config/               # Spring configuration
│   ├── controller/           # REST controllers (auth + home)
│   ├── service/              # Business logic (mixed concerns)
│   ├── repository/           # Data access (JPA + Redis)
│   ├── model/                # Entities and DTOs
│   ├── security/             # JWT filter + header enrichment
│   ├── exception/            # Global exception handler
│   └── util/                 # Common utilities
```

**High-Level Flow (Mermaid)**
```mermaid
flowchart TD
    A[Client / Insomnia] --> B[Spring MVC Controllers]
    B --> C[Security Filter\n(JWT + Custom Headers)]
    C --> D[Auth Service]
    C --> E[Home Service]
    D --> F[Redis Token Cache]
    E --> G[Product Repository\n(In-memory + Postgres)]
    E --> H[Balance Repository]
    B --> I[ResponseDTO Wrapper]
    I --> A
```

---

### 4. Technical Stack

| Category       | Technology                    | Version      | Purpose                          |
|----------------|-------------------------------|--------------|----------------------------------|
| Language       | Java                          | 21           | Primary language                 |
| Framework      | Spring Boot                   | **4.0.6**    | Application framework            |
| Web            | Spring MVC (blocking)         | -            | Legacy-style REST                |
| Security       | Spring Security + JJWT        | 0.12.6+      | JWT + custom headers             |
| Database       | PostgreSQL + Spring Data JPA  | 42.7.x       | Persistent storage               |
| Cache          | Redis (Jedis / Lettuce)       | 5.x          | Token caching                    |
| Build Tool     | Gradle (Kotlin DSL)           | 8.13+        | Build automation                 |
| Test           | JUnit 5 + Mockito + JaCoCo    | -            | Unit tests + coverage            |
| Container      | Docker                        | -            | Local deployment                 |
| Observability  | Spring Boot Actuator          | -            | Health & metrics                 |

**Spring Boot Starters**:
- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-data-redis`
- `spring-boot-starter-security`
- `spring-boot-starter-actuator`
- `spring-boot-starter-validation`
- `spring-boot-starter-test`

---

### 5. Domain Capabilities

**1. Authentication Domain**
- Client login with document number / password (mock data)
- JWT generation with custom claims
- Enrichment of banking headers (`X-RqUid`, `X-SesID`, `X-CustIdentNum`, `X-CustIdentType`, etc.)

**2. Homepage / Product Domain**
- Retrieve aggregated product information for authenticated client
- Include accounts, credit cards, and current balances
- Synchronous calls to repositories (blocking)

---

### 6. API Endpoints

**Base URL**: `http://localhost:8080`

| Endpoint              | Method | Description                              | Required Headers                          | Response          |
|-----------------------|--------|------------------------------------------|-------------------------------------------|-------------------|
| `/api/auth/login`     | POST   | Authenticate client and issue JWT        | `X-CustIdentNum`, `X-CustIdentType`       | `ResponseDTO`     |
| `/api/home/balance`   | GET    | Aggregated homepage products & balances  | All banking headers + `Authorization`     | `ResponseDTO`     |
| `/actuator/health`    | GET    | Health check                             | -                                         | `{ "status": "UP" }` |

**Common Response Format**
```json
{
  "statusCode": 200,
  "body": { ... },
  "extraArgs": { ... }
}
```

---

### 7. Configuration

**Key `application.yml` excerpts:**
```yaml
server:
  port: 8080

spring:
  application:
    name: corebank-monolith
  datasource:
    url: jdbc:postgresql://localhost:5432/corebank
  redis:
    host: localhost
    port: 6379

jwt:
  secret: ${JWT_SECRET:super-secret-for-demo-only}
  expiration: 3600000   # 1 hour
```

**Required Environment Variables**:
- `JWT_SECRET`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

---

### 8. Security

- JWT-based authentication with custom claims
- `OncePerRequestFilter` that validates token and injects exact banking headers
- All endpoints (except health) require valid JWT
- Header enrichment for downstream simulation

---

### 9. Infrastructure & Deployment

- **Dockerfile** (multi-stage, based on `amazoncorretto:21`)
- **docker-compose.yml** (includes Postgres + Redis)
- Local run: `docker compose up --build`

---

### 10. Testing

**Coverage Requirement**: Minimum **80%** line coverage (JaCoCo enforced in build).

**Test Structure**:
- Unit tests for every Service and Controller
- `@WebMvcTest` for controllers
- `@DataJpaTest` + `@SpringBootTest` for integration tests

**Commands**:
```bash
./gradlew clean build
./gradlew jacocoTestReport
```

---

### 11. What This System Does

- Authenticates clients and issues JWT + banking headers
- Returns aggregated homepage data for authenticated clients
- Demonstrates real legacy banking patterns (custom headers, ResponseDTO, Redis token cache)

### 12. What This System Does NOT Do

- Does **not** use Hexagonal Architecture (deliberate tech debt)
- Does **not** use reactive programming (blocking MVC)
- Does **not** separate concerns into microservices
- Does **not** implement Circuit Breaker or advanced resilience (added in Phase 2)

---

### 13. Development Guidelines

- Java 21 + Gradle with **Kotlin DSL** (`build.gradle.kts`)
- Follow standard Spring Boot layered structure
- All business logic must be covered by unit tests
- Run `./gradlew build` before every commit
- Target: **80%+** JaCoCo coverage
- Use records and pattern matching lightly where it improves readability (modern Java)

---

### 14. Monitoring & Observability

- Spring Boot Actuator (`/actuator/health`, `/actuator/metrics`)
- Structured JSON logging
- Redis key visibility for token debugging

---

**End of Phase 1 ERD**

This document serves as the official baseline for the modernization journey. Phase 2 will extract the monolith into clean microservices using Hexagonal Architecture, DDD, and reactive patterns while preserving identical external behavior.

---
