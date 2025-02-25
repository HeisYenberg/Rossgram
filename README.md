# Rossgram

A microservices-based web service inspired by Instagram, built with Java and
Spring ecosystem.

## Features

- **User Management**: Registration, authentication, and profile management
- **Image Collections**: Upload/delete images, organize personal collections
- **Social Features**: Comment on images, delete comments, view public
  collections
- **Resilient Architecture**: Circuit Breaker, Retry patterns, and Health Checks
- **SSR Frontend**: Server-side rendered UI with Thymeleaf
- **Scalable**: Dockerized microservices with NGINX reverse proxy

## Architecture Overview

**Services**:

1. **User Service** (Port 8081) - Authentication & user profiles
2. **Image Service** (Port 8082) - Image storage and management
3. **Comment Service** (Port 8083) - Comment functionality
4. **API Gateway** (Port 8084) - Unified entry point
5. **SSR Frontend** (Port 8085) - Server-side rendered UI with Thymeleaf

## Technologies

- **Java 17** - Core programming language
- **Spring Boot** - Microservices framework
- **Spring Security** - JWT authentication
- **Spring Data** - Working with database
- **Spring Cloud Gateway** - API Gateway implementation
- **Resilience4J** - Circuit Breaker & Retry patterns
- **PostgreSQL** - Relational database
- **Thymeleaf** - Server-side templating
- **Docker** - Containerization

## Getting Started

### Prerequisites

- Docker

### Installation

```bash
git clone https://github.com/HeisYenberg/Rossgram.git
cd Rossgram
docker-compose up
```