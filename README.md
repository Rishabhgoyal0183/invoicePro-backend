# InvoicePro Backend

## Overview
InvoicePro is a Spring Boot backend for managing businesses, customers, and products for invoicing workflows. It provides authentication (including Google OAuth-based flows), customer and product management endpoints, and supporting infrastructure for persistence, security, and utilities.

## Tech stack
- **Java 21** (toolchain)
- **Spring Boot 3.5.5**
- **Spring Web, Security, Validation, Data JPA**
- **MySQL** runtime driver
- **JWT** for authentication tokens
- **AWS SDK v2 (SQS/SNS)**

## Build & run
> Note: No `src/main/resources` directory exists in this repo snapshot, so you'll need to supply your own `application.properties`/`application.yml` with database, security, and OAuth settings.

- Build:
  ```bash
  ./gradlew build
  ```
- Run locally:
  ```bash
  ./gradlew bootRun
  ```

## Repository structure

### Root files
- `build.gradle` – Gradle build configuration and dependencies.
- `settings.gradle` – Gradle settings.
- `gradlew`, `gradlew.bat` – Gradle wrapper scripts.
- `gradle/` – Gradle wrapper support files.
- `src/` – Application source code.

### `src/main/java/com/invoicePro`
High-level package map and responsibilities:

| Package | Purpose |
| --- | --- |
| `auth/` | Authentication domain: request DTOs, controllers, and services for login, onboarding, Google auth, and password reset. |
| `aws/` | Placeholder AWS controller integration. |
| `config/` | Spring configuration classes. |
| `controller/` | REST controllers for customer and product APIs. |
| `dto/` | DTOs for API responses and service mappings. |
| `entity/` | JPA entities (Business, Customer, Product, etc.). |
| `enums/` | Enumerations for domain status/type fields. |
| `exception/` | Centralized exceptions and error handling. |
| `mapper/` | Entity/DTO mapping utilities. |
| `repository/` | Spring Data JPA repositories. |
| `request/` | Request DTOs for API input. |
| `response/` | Standardized API response wrappers. |
| `security/` | JWT, filters, user details, and session handling. |
| `service/` | Service interfaces for domain logic. |
| `service/impl/` | Service implementations. |
| `utils/` | Shared utility helpers. |
| `validator/` | Request validation helpers. |

### `src/test/java`
- `com/invoicePro/InvoiceProApplicationTests.java` – Spring Boot test bootstrap.

## Core business flow (high level)
1. **Authentication & onboarding**
   - Google auth verify + onboarding: creates BusinessOwner and Business.
   - Username/password login generates a JWT and session.
2. **Business-scoped APIs**
   - Customers: list, create, update, status change, soft delete, and metrics.
   - Products: list, create, update, delete, and metrics.

## API groups (at a glance)
- `POST /api/auth/login`
- `GET /api/auth/logout`
- `GET /api/auth/google/verify-email`
- `POST /api/auth/onboarding/register`
- `GET /api/auth/google/forgot-password`
- `POST /api/auth/change-password`
- `GET /api/businesses/{businessId}/customers`
- `POST /api/businesses/{businessId}/customers/save`
- `GET /api/businesses/{businessId}/customers/{customerId}`
- `PUT /api/businesses/{businessId}/customers/{customerId}`
- `PUT /api/businesses/{businessId}/customers/{customerId}/change-status`
- `DELETE /api/businesses/{businessId}/customers/{customerId}/delete`
- `GET /api/businesses/{businessId}/customers/metrics`
- `GET /api/businesses/{businessId}/products`
- `POST /api/businesses/{businessId}/products/save`
- `GET /api/businesses/{businessId}/products/{productId}`
- `PUT /api/businesses/{businessId}/products/{productId}/update`
- `DELETE /api/businesses/{businessId}/products/{productId}/delete`
- `GET /api/businesses/{businessId}/products/metrics`
- `GET /api/aws/test`
