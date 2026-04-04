# 📊 Finance Data Processing & Access Control Backend

Hello! This is my submission for the Finance Data Processing and Access Control Backend assignment. I have built a robust, enterprise-grade REST API that manages financial records, calculates aggregated summaries, and enforces strict Role-Based Access Control (RBAC).

This README serves as both the setup guide and the architectural design document, outlining my engineering decisions against the core evaluation criteria.

---

## 🚀 Quick Start & Local Setup

This project uses an in-memory **H2 database**, meaning **no external database installation is required.** It is completely self-contained for easy evaluation.

### 1. Run the Application
Open your terminal and run:
```bash
./mvnw spring-boot:run
```
The application will start on `http://localhost:8080`.

### 2. Seeded Accounts (for evaluation)
On startup, a `DatabaseSeeder` automatically creates three default users with different roles so you can test the RBAC immediately. Passwords are encrypted using BCrypt.

* **Admin** (Username: `admin` | Password: `admin123`) - Full access to all endpoints.
* **Analyst** (Username: `analyst` | Password: `analyst123`) - Can view records and dashboard summaries.
* **Viewer** (Username: `viewer` | Password: `viewer123`) - Can only view financial records.

### 3. API Documentation & Testing
I have integrated **Swagger / OpenAPI 3.0**. Once the server is running, navigate to:
👉 `http://localhost:8080/swagger-ui.html`

**How to authenticate in Swagger:**
1. Call `POST /api/v1/auth/login` with one of the seeded accounts (e.g., `admin` / `admin123`).
2. Copy the `token` from the response.
3. Scroll to the top of Swagger UI, click **Authorize** (the padlock icon), type `Bearer YOUR_TOKEN`, and click Authorize.

---

## 📋 Evaluating My Submission

Here is how my implementation addresses the 7 core evaluation criteria outlined in the assignment.

### 1. Backend Design
**How well the application is structured, including routes, services, models, and separation of concerns.**
I structured the application using a classic layered architecture (Controller ➡️ Service ➡️ Repository) to ensure a clean separation of concerns:
* **Controllers** are kept thin, handling only HTTP routing, DTO mapping, and security checks (`@PreAuthorize`).
* **Services** contain all business logic so that rules are isolated and reusable.
* **Repositories** extend `JpaRepository` for data access, keeping SQL logic out of the service layer.
* I completely separated **Entities** (Database Models) from **DTOs** (API Payloads) to prevent over-posting attacks and ensure that the API contract remains stable even if the database schema changes.

### 2. Logical Thinking
**How clearly business rules, access control, and data processing have been implemented.**
I implemented stateless **JWT Authentication** as it represents an industry-standard mechanism for modern web apps.
* **Access control** is enforced at the method level using Spring Security's `@PreAuthorize` mapped to three precise roles (`VIEWER`, `ANALYST`, `ADMIN`).
* A dedicated **Dashboard Summary API** efficiently aggregates data (Totals, Net Balance, Category Breakdowns) directly using SQL/JPQL `SUM` and `GROUP BY` functions instead of expensive in-memory Java calculations.

### 3. Functionality
**Whether the expected APIs and backend features work correctly and consistently.**
All expected backend features are fully working:
* **Auth**: Secure login to obtain JWT tokens.
* **Finance**: Full CRUD for financial entries with category/type specifications.
* **Users**: An admin-only user management module to create, update, list, and softly deactivate users.
* APIs strictly follow REST conventions (`POST` for creation, `GET` for retrieval, `DELETE` for removal, returning proper HTTP status codes like `201 CREATED` and `204 NO CONTENT`).

### 4. Code Quality
**Readability, maintainability, naming, organization, and general coding practices.**
* **Lombok** is used to reduce boilerplate code (Getters, Setters, Constructors, Builders), making the classes clean and easy to read.
* Variables, models, and endpoints are named intuitively following standard Java casing conventions.
* Strict package organization (`config`, `controller`, `dto`, `exception`, `model`, `repository`, `service`) keeps the project naturally discoverable for new developers.

### 5. Database and Data Modeling
**How appropriately data is modeled and managed for the use case.**
* **Precision Currency Handling**: I explicitly used `BigDecimal` (with precision=15, scale=2) for all financial amounts. In financial applications, `double` or `float` represents a critical flaw due to floating-point rounding errors; `BigDecimal` ensures exact correctness.
* **Type Safety**: Used Java Enums (`RecordType`, `Role`) mapped as strings in the database to prevent manual string-matching errors.
* **Soft Deletion**: To preserve critical audit trails, I implemented soft deletion. Financial records and users are flagged as `is_deleted` or `isActive = false` instead of being permanently dropped. JPA uses a `@SQLRestriction("is_deleted = false")` to seamlessly exclude them from regular queries.

### 6. Validation and Reliability
**How well the application handles bad input, invalid states, and error conditions.**
I implemented robust input validation and error handling to ensure the backend never hits an invalid state:
* Complete request validation on all DTOs using Jakarta Validator tags (`@NotNull`, `@Positive`, `@PastOrPresent`, `@Size`, `@NotBlank`).
* Created a **GlobalExceptionHandler** (`@RestControllerAdvice`) that catches `MethodArgumentNotValidException` and custom `ResourceNotFoundExceptions`. It safely intercepts bad inputs and returns formatted, predictable JSON error payloads mapping fields to error messages.

### 7. Documentation
**Clarity of the README, setup process, API explanation, assumptions made, and any tradeoffs considered.**
I made a few key assumptions to balance an optimal evaluation experience with industrial standards:
* **Tradeoff - In-Memory DB**: I chose the H2 database for simplicity so reviewers wouldn't have to spin up a Docker container or a Postgres instance locally. The tradeoff is that data is ephemeral (lost on restart). To switch to Postgres for production, one only needs to update `application.properties`.
* **Assumption - JWT Secret Location**: The JWT signing key is configured in `application.properties` for the sake of the assignment. In a real-world scenario, this would be injected securely via a platform secrets manager (e.g., AWS KMS or HashiCorp Vault) or CI/CD environment variables.
* **Tradeoff - Auth Flow**: I implemented a single-token JWT flow. If this were a long-living production app, I would add a short-lived Access Token and long-lived Refresh Token mechanism to improve session security.

---

Thank you for reviewing my submission! I look forward to discussing my database design, architectural choices, and coding practices with the team.
