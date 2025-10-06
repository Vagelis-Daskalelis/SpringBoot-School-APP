# SpringBoot School App

# ⚙️ Application Info
# - Name: SpringBoot School App
# - Spring Boot Version: 3.5.6
# - Runs on Port: 8089
# - Swagger UI (for testing APIs): http://localhost:8089/swagger-ui/index.html#/

# 📦 Dependencies
# <!-- JWT -->
# <dependency>
#     <groupId>io.jsonwebtoken</groupId>
#     <artifactId>jjwt-api</artifactId>
#     <version>${jjwt.version}</version>
# </dependency>
# <dependency>
#     <groupId>io.jsonwebtoken</groupId>
#     <artifactId>jjwt-impl</artifactId>
#     <version>${jjwt.version}</version>
#     <scope>runtime</scope>
# </dependency>
# <dependency>
#     <groupId>io.jsonwebtoken</groupId>
#     <artifactId>jjwt-jackson</artifactId>
#     <version>${jjwt.version}</version>
#     <scope>runtime</scope>
# </dependency>
# 
# <!-- Spring Web -->
# <dependency>
#     <groupId>org.springframework.boot</groupId>
#     <artifactId>spring-boot-starter-web</artifactId>
# </dependency>
# 
# <!-- JPA -->
# <dependency>
#     <groupId>org.springframework.boot</groupId>
#     <artifactId>spring-boot-starter-data-jpa</artifactId>
# </dependency>
# 
# <!-- Spring Security -->
# <dependency>
#     <groupId>org.springframework.boot</groupId>
#     <artifactId>spring-boot-starter-security</artifactId>
# </dependency>
# 
# <!-- Validation -->
# <dependency>
#     <groupId>org.springframework.boot</groupId>
#     <artifactId>spring-boot-starter-validation</artifactId>
# </dependency>
# 
# <!-- MySQL Driver -->
# <dependency>
#     <groupId>com.mysql</groupId>
#     <artifactId>mysql-connector-j</artifactId>
#     <scope>runtime</scope>
# </dependency>
# 
# <!-- Lombok (optional) -->
# <dependency>
#     <groupId>org.projectlombok</groupId>
#     <artifactId>lombok</artifactId>
#     <optional>true</optional>
# </dependency>
# 
# <!-- Testing -->
# <dependency>
#     <groupId>org.springframework.boot</groupId>
#     <artifactId>spring-boot-starter-test</artifactId>
#     <scope>test</scope>
# </dependency>
# <dependency>
#     <groupId>org.springframework.security</groupId>
#     <artifactId>spring-security-test</artifactId>
#     <scope>test</scope>
# </dependency>
# 
# <!-- Utilities -->
# <dependency>
#     <groupId>org.apache.commons</groupId>
#     <artifactId>commons-lang3</artifactId>
#     <version>3.14.0</version>
# </dependency>
# 
# <!-- Email Support -->
# <dependency>
#     <groupId>org.springframework.boot</groupId>
#     <artifactId>spring-boot-starter-mail</artifactId>
# </dependency>
# 
# <!-- Swagger -->
# <dependency>
#     <groupId>org.springdoc</groupId>
#     <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
#     <version>2.2.0</version>
# </dependency>
# 
# <!-- PDF Generation -->
# <dependency>
#     <groupId>com.github.librepdf</groupId>
#     <artifactId>openpdf</artifactId>
#     <version>1.3.8</version>
# </dependency>
# <dependency>
#     <groupId>com.itextpdf</groupId>
#     <artifactId>itextpdf</artifactId>
#     <version>5.5.13.3</version>
# </dependency>

# 🌟 Features
# - User roles: ADMIN, TEACHER, STUDENT
# - User account status: ACTIVE or BANNED
# - JWT authentication for secure endpoints
# - CRUD operations for Users, Students, Teachers, Courses, Specialities, Images
# - Image upload and management for users
# - PDF generation for teachers, students and student course reports
# - Email notifications (via Spring Mail)
# - Swagger documentation for API testing

# 🔗 Main Endpoints
# - `/api/user/**` : Registration & login (open to all)
# - `/api/student/**` : Student-specific actions (requires STUDENT role)
# - `/api/teacher/**` : Teacher-specific actions (requires TEACHER or ADMIN role)
# - `/api/admin/**` : Admin-only actions
# - `/api/course/**` : Course management (requires ADMIN role)
# - `/api/speciality/**` : Specialities management (ADMIN only)
# - `/api/image/**` : Image upload/download (requires any authenticated role)
# - `/api/pdf/**` : Generate PDFs for student courses (requires any authenticated role)

# 🔐 Security Notes
# - JWT-based authentication is applied to all endpoints except `/api/user/**` and Swagger UI.
# - Users with `BANNED` status cannot log in or access any protected endpoints.
# - Swagger UI URL for testing: http://localhost:8089/swagger-ui/index.html#/
# - Spring Security automatically enforces roles and status for API requests.

# ⚡ Running the Application
# 1. Configure `application.properties` with your MySQL database credentials.
# 2. Build and run the Spring Boot app.
# 3. Access Swagger UI for API testing at http://localhost:8089/swagger-ui/index.html#/
# 4. Use JWT tokens for authorized requests to protected endpoints.
