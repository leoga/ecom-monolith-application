# eCommerce Application

Monolithic e-commerce application built with **Spring Boot 4.0.3** and **Java 21**, fully dockerized with PostgreSQL as the database.

## 🚀 Features

- Complete user management
- Product catalog with search functionality
- Shopping cart
- Order system
- RESTful API
- PostgreSQL database
- Database administration panel with pgAdmin
- Actuator for monitoring and metrics

## 📋 Prerequisites

- Docker and Docker Compose
- Java 21 (for local development)
- Maven 3.x (for local development)

## 🐳 Installation and Execution with Docker

1. Clone the repository:
```bash
git clone <repository-url>
cd ecom-application
```

2. Start services with Docker Compose:
```bash
docker-compose up -d
```

This will start:
- **PostgreSQL** on port `5432`
- **pgAdmin** on port `5050`

3. Build and run the application:
```bash
./mvnw clean package
./mvnw spring-boot:run
```

Or on Windows:
```bash
mvnw.cmd clean package
mvnw.cmd spring-boot:run
```

## 🔧 Configuration

### Database
The application connects to PostgreSQL with the following configuration (defined in `application.properties`):

- **URL**: `jdbc:postgresql://localhost:5432/leoga`
- **Username**: `leoga`
- **Password**: `leoga`

### pgAdmin
Access pgAdmin at `http://localhost:5050`:
- **Email**: `pgadmin4@pgadmin.org`
- **Password**: `admin`

## 📊 Actuator Endpoints

The application exposes several monitoring endpoints:

- **Health**: `http://localhost:8080/actuator/health`
- **Info**: `http://localhost:8080/actuator/info`
- **Metrics**: `http://localhost:8080/actuator/metrics`
- **Beans**: `http://localhost:8080/actuator/beans`

## 📚 API Documentation

For more details about available endpoints, see [API_DOCUMENTATION.md](API_DOCUMENTATION.md)

## 🏗️ Architecture

This is a **monolithic** application with the following structure:

```
ecom-application/
├── src/
│   ├── main/
│   │   ├── java/com/ecom/leoga/app/
│   │   │   ├── controllers/     # REST Controllers
│   │   │   ├── services/        # Business Logic
│   │   │   ├── repositories/    # Data Access
│   │   │   ├── model/           # JPA Entities
│   │   │   └── dto/             # Data Transfer Objects
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── docker-compose.yml
└── pom.xml
```

## 🛠️ Technologies

- **Framework**: Spring Boot 4.0.3
- **Language**: Java 25
- **Database**: PostgreSQL 18
- **ORM**: Hibernate/JPA
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose
- **Additional Dependencies**:
  - Lombok
  - Spring Boot Actuator
  - Spring Data JPA

## 📝 License

This project is part of a training course and is available for educational purposes.

## 👤 Author

Leoga
