# TalentPulse — Enterprise HR & Workforce Intelligence Platform

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=flat-square&logo=spring&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Security-6-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-15-336791?style=flat-square&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Redis-7-DC382D?style=flat-square&logo=redis&logoColor=white" />
  <img src="https://img.shields.io/badge/RabbitMQ-3-FF6600?style=flat-square&logo=rabbitmq&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-Ready-2496ED?style=flat-square&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/License-MIT-22C55E?style=flat-square" />
</p>

> **Enterprise HR & Workforce Intelligence Platform** — employee lifecycle management, 360° performance reviews, AI-powered attrition risk scoring, workforce planning, and job requisition pipeline — event-driven via RabbitMQ with JWT-secured REST APIs.

---

## Overview

TalentPulse gives HR teams and business leaders a unified platform for the full employee lifecycle — from candidate tracking to offboarding. The platform calculates attrition risk scores for every active employee using a weighted model across tenure, performance, engagement, and absence data. Workforce planning dashboards surface headcount gaps by department and role. All state transitions publish events to RabbitMQ, enabling real-time notifications for onboarding tasks, review deadlines, and offboarding checklists.

Designed for **enterprise HR departments, people analytics teams, and talent acquisition organizations** that need a scalable, observable HR data platform.

---

## Architecture

```
┌──────────────────────────────────────────────────────────────────┐
│                      TalentPulse Platform                        │
│                                                                  │
│  ┌────────────┐  ┌─────────────┐  ┌────────────┐  ┌─────────┐  │
│  │  Employee  │  │ Performance │  │ Workforce  │  │  Auth   │  │
│  │    API     │  │    API      │  │  Planning  │  │   API   │  │
│  └─────┬──────┘  └──────┬──────┘  └─────┬──────┘  └─────────┘  │
│        │                │               │                        │
│  ┌─────▼────────────────▼───────────────▼───────────────────┐   │
│  │                   Spring Boot 3.2 Service Layer           │   │
│  │                                                           │   │
│  │  ┌──────────────────┐   ┌──────────────────────────────┐  │   │
│  │  │ Employee Service  │   │ Workforce Planning Service   │  │   │
│  │  │ Performance Svc   │   │ Attrition Risk Scoring       │  │   │
│  │  └─────────┬─────────┘   └──────────────────────────────┘  │   │
│  └────────────┼──────────────────────────────────────────────┘   │
│               │                                                   │
│  ┌────────────▼────────────────────────────────────────────┐     │
│  │   PostgreSQL 15  ·  Redis 7 Cache  ·  RabbitMQ 3        │     │
│  │   employees · reviews · requisitions · skills            │     │
│  └─────────────────────────────────────────────────────────┘     │
└──────────────────────────────────────────────────────────────────┘
```

---

## Features

### 1. Employee Lifecycle Management
Full CRUD for employee records with status transitions: ACTIVE, ON_LEAVE, PROBATION, TERMINATED. Track department, role, manager, hire date, salary band, and skill inventory. Status changes publish events to RabbitMQ.

### 2. 360° Performance Review Cycles
Create review cycles with configurable rating dimensions (PRODUCTIVITY, QUALITY, COMMUNICATION, LEADERSHIP, TECHNICAL_SKILLS). Collect self-assessments, manager reviews, and peer feedback. Aggregate into composite performance scores per review period.

### 3. Attrition Risk Scoring
Every employee receives an attrition risk score (0–100) calculated weekly using a weighted model:

| Factor | Weight | Signal |
|--------|--------|--------|
| Performance trend | 30% | Declining scores → higher risk |
| Tenure band | 20% | 1–2 years → peak voluntary attrition |
| Absence rate | 20% | High unplanned absences → disengagement |
| Compensation band | 15% | Below market → retention risk |
| Promotion recency | 15% | >24 months without promotion → risk |

**Risk Tiers:** LOW (0–24) · MEDIUM (25–49) · HIGH (50–74) · CRITICAL (75–100)

### 4. Workforce Planning & Headcount Forecasting
Model planned vs. actual headcount by department and role. Track open requisitions, time-to-fill metrics, and projected attrition to produce 6-month headcount forecasts.

### 5. Job Requisition Pipeline
Manage job openings with workflow: DRAFT → APPROVED → OPEN → INTERVIEWING → OFFER → FILLED · CANCELLED. Track candidates, interview stages, and hiring decisions.

### 6. Event-Driven Notifications
| Event | RabbitMQ Exchange | Consumer Action |
|-------|------------------|-----------------|
| `employee.onboarded` | `hr.events` | Trigger onboarding checklist |
| `review.due` | `hr.events` | Notify manager + employee |
| `attrition.risk.high` | `hr.alerts` | Alert HR business partner |
| `employee.offboarded` | `hr.events` | Trigger offboarding workflow |

---

## API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/v1/auth/register` | Public | Register user |
| POST | `/api/v1/auth/login` | Public | Login, receive JWT |
| POST | `/api/v1/employees` | HR_ADMIN | Create employee record |
| GET | `/api/v1/employees` | Required | List employees with filters |
| GET | `/api/v1/employees/{id}` | Required | Get employee detail |
| PUT | `/api/v1/employees/{id}` | HR_ADMIN | Update employee record |
| PUT | `/api/v1/employees/{id}/status` | HR_ADMIN | Update employment status |
| POST | `/api/v1/performance/reviews` | Manager/Admin | Create performance review |
| GET | `/api/v1/performance/employees/{id}` | Required | Get employee review history |
| GET | `/api/v1/workforce/attrition-risk` | HR_ADMIN | Get attrition risk rankings |
| POST | `/api/v1/requisitions` | HR_ADMIN | Create job requisition |
| GET | `/api/v1/requisitions` | Required | List open requisitions |
| GET | `/health` | Public | Health check |

---

## Getting Started

```bash
git clone https://github.com/Gokatech-Inc/TalentPulse.git
cd TalentPulse

docker-compose up -d

# API:  http://localhost:8080
# Docs: http://localhost:8080/swagger-ui.html
```

### Local Development
```bash
mvn spring-boot:run
```

### Running Tests
```bash
mvn test
```

---

## Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/talentpulse` | Database URL |
| `jwt.secret` | (required) | HS256 signing secret |
| `jwt.expiration` | `86400000` | Token TTL (ms) |
| `spring.rabbitmq.host` | `localhost` | RabbitMQ host |
| `spring.data.redis.host` | `localhost` | Redis host |

---

## License

MIT License — **Gokatech Inc** · HR Technology
