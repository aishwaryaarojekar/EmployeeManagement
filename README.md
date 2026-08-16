# JAVA-W4-004 — Enterprise Employee & Resource Management System

A Spring Boot 3 backend implementing the Week 4 corporate project spec: employees, departments,
projects, resource allocation, leave management, JWT auth with RBAC, dashboard APIs, global
exception handling, auditing, and Swagger docs.

Everything in this project was written from scratch for you — none of the sample code snippets
from the brief were copy-pasted.

## 1. Import into STS4

1. Unzip the project.
2. In STS4: **File → Import → Maven → Existing Maven Projects** → select the `evmpas-w4` folder → Finish.
3. Wait for Maven to download dependencies (needs internet access — STS4's environment, not this sandbox).
4. Run `EvmpasApplication.java` as a **Spring Boot App** (right-click → Run As).

That's it — **no database setup required to try it out.** The app ships with an in-memory H2
database by default (`application.properties`), so it runs immediately.

On startup you'll see in the console:
```
>>> Default admin user created -> username: admin | password: admin123
```

## 2. Try it immediately

- API base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html` (click "Authorize" and paste `Bearer <token>` after logging in)
- H2 console (to inspect data): `http://localhost:8080/h2-console` — JDBC URL `jdbc:h2:mem:evmpasdb`, user `sa`, blank password
- Postman: import `EVMPAS-W4.postman_collection.json`, set the `token` variable after logging in

**Login first:**
```
POST /api/auth/login
{ "username": "admin", "password": "admin123" }
```
Copy the `token` from the response and send it as `Authorization: Bearer <token>` on every other request.

Typical flow to test everything end-to-end:
1. Login as `admin` → get token
2. `POST /api/departments` → create a department
3. `POST /api/employees` → create an employee in that department
4. `POST /api/projects` → create a project
5. `POST /api/allocations` → allocate the employee to the project (try >100% to see the business-rule error)
6. `POST /api/leaves` → submit a leave request, then `PATCH /api/leaves/{id}/decision` to approve/reject
7. `GET /api/dashboard/summary` → see aggregated metrics

## 3. Switching to real MySQL

When you're ready to use MySQL instead of H2:

1. Create the database (or let Hibernate do it): `CREATE DATABASE enterprise_management;`
2. Update credentials in `src/main/resources/application-mysql.properties` if yours differ from `root/root`.
3. Run with the `mysql` profile:
   - In STS4: Run Configurations → Arguments tab → VM arguments: `-Dspring.profiles.active=mysql`
   - Or command line: `mvn spring-boot:run -Dspring-boot.run.profiles=mysql`

Hibernate will auto-create all tables (`ddl-auto=update`). A reference schema is also included at
`database/schema-reference.sql` if you want to inspect or hand-run the DDL.

## 4. Roles & access control

| Role | Can do |
|---|---|
| `ADMIN` | Everything |
| `HR_MANAGER` | Employees, leave approvals |
| `PROJECT_MANAGER` | Projects, resource allocation |
| `EMPLOYEE` | Own profile, submit leave |

Register more users via `POST /api/auth/register` with the desired `role`.

## 5. What's implemented

- **Module 1** Auth: register/login, BCrypt password hashing, JWT issuance & validation, stateless sessions, role-based `@PreAuthorize` on every write endpoint
- **Module 2** Employees: full CRUD, soft-delete (deactivate, not physical delete)
- **Module 3** Departments: full CRUD, soft-delete
- **Module 4** Projects: CRUD + dedicated `PATCH /status` endpoint, status enum (`PLANNED/ACTIVE/ON_HOLD/COMPLETED/CANCELLED`)
- **Module 5** Resource Allocation: enforces employee's total active allocation across projects never exceeds 100%, blocks duplicate active allocation to the same project, blocks allocating to inactive employees or closed projects — all inside a single `@Transactional` method so a failure rolls back cleanly
- **Module 6** Leave Management: submit → pending → approve/reject workflow with `approvedBy`/`approvedDate` tracking
- **Module 7** Search & filter: `GET /api/employees?department=&designation=&location=&status=` built with `JpaSpecificationExecutor`, plus pagination & sorting (`page`, `size`, `sort`)
- **Module 8** Dashboard: `GET /api/dashboard/summary` — totals, active/inactive counts, breakdown by department & location, project counts by status, leave counts
- DTO layer throughout (entities are never returned directly from controllers)
- Jakarta Bean Validation on all request DTOs
- `GlobalExceptionHandler` with typed exceptions (`ResourceNotFoundException`, `DuplicateResourceException`, `BusinessException`, etc.) and a consistent JSON error shape
- Standardized `ApiResponse<T>` wrapper (`success`, `message`, `data`, `timestamp`) on every endpoint
- Audit logging (`audit_logs` table) for login, register, and other key actions
- Swagger/OpenAPI docs with JWT "Authorize" support
- 10 unit tests (JUnit 5 + Mockito) covering the resource-allocation business rules, leave workflow rules, and duplicate/not-found paths for employees & departments — add more under `src/test/java` following the same pattern for full coverage of the "minimum 20 test cases" target

## 6. Not included (marked optional in the brief)

Email notifications, Docker/Redis, and Actuator metrics were left out to keep the project focused
and runnable out of the box — say the word and I'll add any of them next.

## 7. Project structure

```
com.company.enterprise
├── controller      REST endpoints
├── service          interfaces
├── service.impl      implementations (business logic + @Transactional)
├── repository        Spring Data JPA repositories
├── entity             JPA entities
├── dto                 request/response DTOs
├── mapper              entity <-> DTO mapping
├── security             JWT filter, JwtUtil, UserDetailsService
├── specification        dynamic query building for employee search
├── exception             typed exceptions + GlobalExceptionHandler
├── config                 SecurityConfig, OpenApiConfig, DataSeeder
└── util                    small helpers (code generation)
```
