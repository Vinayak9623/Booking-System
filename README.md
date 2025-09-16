#  BookWise Reservation System

BookWise is a robust resource reservation system built with Spring Boot, supporting role-based access control using JWT authentication. It supports two user roles:

- `ADMIN`: Full control over resources and reservations.
- `USER`: Can view resources, create reservations, and view their own reservations.
## 🚀 Features

- ✅ JWT Authentication & Authorization
- ✅ Role-Based Access Control (RBAC)
- ✅ Resource & Reservation Management
- ✅ User Management with Encrypted Passwords
- ✅ Pagination & Sorting
- ✅ Filter Reservations by Status & Price Range
- ✅ Swagger API Documentation
- ✅ MySQL Integration

## 🧰 Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Security (JWT)
- Hibernate / JPA
- MySQL 8
- Lombok
- Swagger (OpenAPI)
- Maven

## ⚙️ Environment Setup

### 🔐 `application.yml` Configuration

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bookwise_db
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
jwt:
  secret: 5367566B5970337336763979244226452948404D635166546A576E5A7234753778214125442A472D4B6150645367566B5970
  expiration: 3600000
