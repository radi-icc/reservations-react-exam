# PID Backend — Theatre Reservation & Cultural Events Platform

## Project Overview

PID Backend is a Spring Boot REST API designed for managing theatre shows, cultural events, reservations, reviews, affiliate integrations, and administrative operations.

The platform allows visitors to browse cultural events and performances while authenticated users can create reservations, submit reviews, and interact with the system according to their roles.

The backend also supports:

* JWT Authentication
* Role-based access control
* CSV import/export
* External API integration
* RSS feeds
* Swagger/OpenAPI documentation
* Pagination, filtering, and searching
* Affiliate API access using API keys
* Producer statistics and reporting

---

# Technologies Used

## Backend

* Java 17
* Spring Boot 3
* Spring Security
* Spring Data JPA
* Hibernate
* Maven
* Lombok

## Database

* MySQL

## Authentication

* JWT (JSON Web Tokens)
* BCrypt Password Encryption

## Documentation

* Swagger / OpenAPI

## Testing

* JUnit 5
* Mockito
* Postman

## External Integrations

* Brussels OpenData API
* RSS Feed generation

---

# Main Features

## Authentication & Authorization

* User signup
* User login
* JWT token generation
* BCrypt password hashing
* Role-based access control

## User Roles

The application supports the following roles:

* ADMIN
* MEMBER
* PRODUCER
* CRITIC
* AFFILIATE

## Show Catalogue

* Create shows
* Update shows
* Delete shows
* Browse public catalogue
* Search shows
* Filter shows
* Paginated results

## Reservations

* Create reservations
* Cancel reservations
* Seat availability validation
* Capacity management
* Reservation statistics

## Reviews

* Create reviews
* Publish/unpublish reviews
* Public review listing

## Artist Management

* Artists
* Artist types
* Artist type assignments
* Collaborations

## CSV Import / Export

* Export shows to CSV
* Import shows from CSV

## External API Integration

* Import cultural venue/show data from Brussels OpenData API

## RSS Feed

* Upcoming performances RSS feed

## Affiliate APIs

* API key generation
* Affiliate catalogue access
* Plan-based access control

## Statistics

* Revenue statistics
* Reservation statistics
* Show sales statistics

---

# Database Entities

Main entities used in the project:

* users
* roles
* localities
* locations
* artists
* artist_types
* artist_type_assignments
* collaborations
* prices
* shows
* representations
* reservations
* representation_reservations
* reviews
* affiliate_plans
* api_keys

---

# Project Structure

```text
src/main/java/com/pid/backend
│
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service
└── BackendApplication.java
```

---

# API Documentation

Swagger UI:

```text
http://localhost:8085/swagger-ui/index.html
```

OpenAPI Docs:

```text
http://localhost:8085/v3/api-docs
```

---

# Setup Instructions

## 1. Clone Repository

```bash
git clone <repository-url>
cd backend
```

---

## 2. Configure Database

Create MySQL database:

```sql
CREATE DATABASE pid_backend;
```

---

## 3. Configure application.properties

Example configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pid_backend
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8085

jwt.secret=change-this-secret-key-to-at-least-32-characters-long
jwt.expiration-ms=86400000

external.shows.api-url=https://opendata.brussels.be/api/explore/v2.1/catalog/datasets/lieux_culturels_touristiques_evenementiels_visitbrussels_vbx/records?limit=20
```

---

## 4. Install Dependencies

```bash
mvn clean install
```

---

## 5. Run Application

```bash
mvn spring-boot:run
```

Backend starts at:

```text
http://localhost:8085
```

---

# Authentication Flow

## Signup

```http
POST /api/auth/signup
```

Request:

```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "Password123!",
  "confirmPassword": "Password123!",
  "firstname": "John",
  "lastname": "Doe",
  "language": "en"
}
```

---

## Login

```http
POST /api/auth/login
```

Request:

```json
{
  "usernameOrEmail": "john",
  "password": "Password123!"
}
```

Response:

```json
{
  "token": "jwt-token",
  "tokenType": "Bearer"
}
```

---

# JWT Usage

Authenticated requests must include:

```text
Authorization: Bearer YOUR_TOKEN
```

---

# Public Endpoints

Examples:

```text
GET /api/shows
GET /api/locations
GET /api/reviews
GET /api/rss/upcoming-representations
```

---

# Protected Endpoints

Examples:

```text
POST /api/reservations
POST /api/reviews
GET /api/auth/me
```

---

# Example Endpoints

## Shows

```http
GET /api/shows
```

```http
POST /api/shows
```

---

## Reservations

```http
POST /api/reservations
```

```http
PATCH /api/reservations/{id}/cancel
```

---

## Reviews

```http
POST /api/reviews
```

```http
PATCH /api/reviews/{id}/publish
```

---

# CSV Import / Export

## Export Shows

```http
GET /api/admin/csv/shows/export
```

---

## Import Shows

```http
POST /api/admin/csv/shows/import
```

CSV format:

```csv
title,posterUrl,bookable,price,description,locationId
Ayiti,/wrapped/imgs/ayiti.jpg,true,25.00,A theatre show,1
```

---

# External API Import

Imports cultural/event data from Brussels OpenData API.

Endpoint:

```http
POST /api/admin/external-shows/import?defaultLocationId=1
```

The imported records are automatically inserted into the `shows` table.

---

# RSS Feed

Upcoming performances RSS feed:

```http
GET /api/rss/upcoming-representations
```

Returns XML RSS content.

---

# Affiliate API Access

Affiliate APIs use:

```text
X-API-KEY: your-api-key
```

Endpoint:

```http
GET /api/affiliate/shows
```

---

# Statistics APIs

## General Statistics

```http
GET /api/statistics
```

## Show Sales Statistics

```http
GET /api/statistics/shows/{showId}/sales
```

---

# Testing

## Run Unit Tests

```bash
mvn test
```

## Run Full Build

```bash
mvn clean install
```

---

# Postman Testing

The backend was tested using Postman for:

* Authentication
* CRUD operations
* Reservations
* Reviews
* CSV import/export
* External API import
* RSS feed
* Statistics
* Swagger

---

# Security Notes

Passwords are encrypted using BCrypt.

JWT authentication is used for protected endpoints.

Role-based access control is implemented using Spring Security.

---

# Future Improvements

Possible future enhancements:

* Docker deployment
* Cloud deployment
* Email notifications
* Payment integration
* Real-time seat updates
* Frontend admin dashboard
* Redis caching
* Rate limiting
* WebSocket notifications

---

# Author

Developed as part of the PID Integration Development Project.

Backend developed using Spring Boot and MySQL.
