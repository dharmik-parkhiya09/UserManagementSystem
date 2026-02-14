USER MANAGEMENT SYSTEM (SPRING BOOT + JWT)

A secure User Management System built using Spring Boot
with JWT Authentication and Role-Based Authorization.

----------------------------------------------------
FEATURES
----------------------------------------------------

- User Registration
- Login with JWT
- Stateless Authentication
- Role-Based Authorization (USER / ADMIN)
- Custom JWT Filter
- Custom Authentication Entry Point
- BCrypt Password Encryption
- Global Exception Handling

----------------------------------------------------
TECH STACK
----------------------------------------------------

- Java 17+
- Spring Boot
- Spring Security
- JWT (io.jsonwebtoken)
- Spring Data JPA
- Hibernate
- MySQL
- Maven

----------------------------------------------------
AUTHENTICATION FLOW
----------------------------------------------------

1. User registers using:
   POST /auth/register

2. User logs in using:
   POST /auth/login

3. Server generates JWT token.

4. Client sends token in header:

   Authorization: Bearer <token>

5. JwtAuthFilter validates token.

6. SecurityContext is set.

7. Protected endpoints become accessible.

----------------------------------------------------
ROLE SYSTEM
----------------------------------------------------

Database Roles:
- ROLE_USER
- ROLE_ADMIN

Security Rules:
- /auth/**      → Public
- /admin/**     → ADMIN only
- other APIs    → Authenticated users

----------------------------------------------------
CONFIGURATION
----------------------------------------------------

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password

jwt.secretKey=your_secret_key_here

----------------------------------------------------
HOW TO RUN
----------------------------------------------------

1. Clone the repository:

   git clone https://github.com/dharmik-parkhiya09/UserManagementApp.git

2. Navigate to project:

   cd UserManagementApp

3. Run application:

   mvn spring-boot:run

   OR

   ./mvnw spring-boot:run

----------------------------------------------------
API TESTING (POSTMAN)
----------------------------------------------------

REGISTER:
POST /auth/register

Body:
{
"username": "testuser",
"password": "password"
}

LOGIN:
POST /auth/login

Body:
{
"username": "testuser",
"password": "password"
}

Response:
{
"id": 1,
"username": "testuser",
"roles": ["ROLE_USER"],
"token": "your_jwt_token"
}

To access protected endpoints:
Add Header:

Authorization: Bearer <token>

----------------------------------------------------
DATABASE TABLES
----------------------------------------------------

- users
- roles
- user_roles

----------------------------------------------------
LEARNING OUTCOMES
----------------------------------------------------

- Implemented JWT from scratch
- Built custom security filter
- Configured stateless session
- Implemented role-based access control
- Designed layered architecture

----------------------------------------------------
AUTHOR
----------------------------------------------------

Dharmik
Spring Boot Backend Developer

====================================================
