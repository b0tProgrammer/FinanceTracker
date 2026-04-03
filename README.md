# 📊 Finance Data Processing & Access Control Backend

A robust, enterprise-grade backend system for a finance dashboard. This API manages financial records, calculates aggregated summaries, and enforces strict Role-Based Access Control (RBAC).

## 🚀 Tech Stack
* **Language:** Java 25
* **Framework:** Spring Boot 4.0.5
* **Security:** Spring Security (Basic Auth for evaluation simplicity)
* **Database:** H2 Database (In-Memory) + Spring Data JPA
* **Documentation:** OpenAPI 2.8.4 / Swagger UI
* **Utilities:** Lombok, Hibernate Validator

## 🧠 Engineering & Design Decisions
To ensure reliability, maintainability, and enterprise standards, the following decisions were made:
1. **Precision Currency Handling:** Used `BigDecimal` for all financial amounts to prevent floating-point rounding errors (never use `double` or `float` for money).
2. **Soft Deletion:** Implemented a `@SQLRestriction("is_deleted = false")` mechanism. Records are never permanently deleted from the database, preserving historical audit trails.
3. **Database-Level Aggregation:** Dashboard summaries (total income, category breakdowns) are calculated directly in the database using JPA `@Query` methods rather than loading all records into Java memory.
4. **Global Exception Handling:** Uses a `@RestControllerAdvice` to intercept validation errors and exceptions, returning a standardized, clean JSON error format.
5. **Separation of Concerns:** Strict isolation between Entities (Database) and DTOs (API responses) to prevent over-posting and accidental data exposure.

## 🔐 Role-Based Access Matrix

| Action | `VIEWER` | `ANALYST` | `ADMIN` |
| :--- | :---: | :---: | :---: |
| View All Records | ✅ | ✅ | ✅ |
| View Dashboard Summary | ❌ | ✅ | ✅ |
| Create New Record | ❌ | ❌ | ✅ |
| Delete Record (Soft) | ❌ | ❌ | ✅ |

## 🛠️ Local Setup & Installation

This project uses an in-memory H2 database, meaning **no external database installation is required.**

1. **Clone the repository:**
   ```bash
   git clone https://github.com/b0tProgrammer/FinanceTracker
   
### 🕵️‍♂️ 2. How to Check Everything is Working Fine

Before you submit your code, you need to act like the reviewer and test it. Here is your exact testing checklist.

**Step 1: Start the Server**
Run your Spring Boot application. Look at the console logs. You should see a line that looks like this:
`[main] c.f.b.config.DatabaseSeeder : Default users seeded successfully! (admin, analyst, viewer | password: [role]123)`
*If you see this, your database seeder worked!*

**Step 2: Open Swagger UI**
Go to your browser and open `http://localhost:8080/swagger-ui.html`. You should see a beautiful, interactive documentation page.

**Step 3: Test the Admin (Full Access)**
1. Click the **Authorize** button. Enter `admin` and `admin123`. Click Authorize, then Close.
2. Go to the `POST /api/v1/finance` endpoint. Click **Try it out**.
3. Paste this JSON into the request body and click **Execute**:
   ```json
   {
     "amount": 1500.00,
     "type": "INCOME",
     "category": "Salary",
     "date": "2023-10-01",
     "notes": "October Salary"
   }