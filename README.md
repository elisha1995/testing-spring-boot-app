# 🚀 Spring Boot Testing Project
## Welcome to My Testing Journey! 👨‍💻
This repository showcases my hands-on project, where I’ve mastered unit and integration testing using JUnit 5, Mockito, Spring Boot, and advanced tools like Testcontainers.

## 📌 Project Overview
This repository contains a Spring Boot application designed to demonstrate robust testing practices across multiple layers:
- **Repository Layer**: Unit tests for data access with Spring Data JPA.
- **Service Layer**: Unit tests for business logic using mocks and assertions.
- **Controller Layer**: Unit tests for REST APIs, ensuring end-to-end functionality.
- **Integration Testing**: Tests with a local MySQL database and Dockerized MySQL via Testcontainers.

The application features an Employee management system, including CRUD operations, which I’ve tested thoroughly to ensure reliability and maintainability.

## 🎯 Key Features & Skills Demonstrated
- ✅ **Unit Testing**: Mocked dependencies with `@MockitoBean` (replacing deprecated ``@MockBean``) and validated behavior using AssertJ assertions.
- ✅ **Integration Testing**: Configured and tested against a real MySQL database, both locally and with Testcontainers for isolated, repeatable environments.
- ✅ **BDD Style**: Structured tests with Given-When-Then patterns for readability and collaboration.
- ✅ **Modern Tools**: Utilized Lombok’s `@Builder` for clean object creation, Spring Boot’s `@WebMvcTest`, and Testcontainers for Docker-based testing.
- ✅ **Problem Solving**: Resolved Maven lock issues during dependency sync, showcasing troubleshooting skills.


Check out the code in the src/test/java directory to see these techniques in action!

## How to Run:
1. 🛠️ Technologies Used
- **Java 21**
- **Spring Boot 3.4.3**
- **JUnit 5**
- **Mockito**
- **Testcontainers**
- Docker (for Testcontainers).
- **MySQL**
2. Setup:
- Clone the repository: git clone https://github.com/elisha1995/testing-spring-boot-app.git.
- Navigate to the project directory: cd your-repo.
- Build the project: mvn clean install.
3. Run Tests:
- Execute all tests: mvn test.
- For integration tests with Testcontainers, ensure Docker is running.
