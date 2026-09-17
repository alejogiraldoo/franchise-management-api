# AGENTS.md

Reactive REST API (Java 21, Spring Boot 4.1.1 WebFlux, Spring Data R2DBC + MySQL) for franchise/branch/product inventory.

## Commands
- Run app: `./mvnw spring-boot:run` (Windows: `./mvnw.cmd ...`). Serves on `http://localhost:8080`.
- Test/build: `./mvnw test` / `./mvnw package`
- DB requires MySQL from `docker-compose.yml`: `docker compose up -d` (root/123456, db `franchise_system`).
- **Tests need the DB**: the only test (`FranchiseSystemApplicationTests`) is a `@SpringBootTest` that loads the full context and connects to MySQL — `mvnw test` fails if the container isn't up.

## Architecture & routing (important)
- Endpoints are **functional**, not annotated controllers: `api/routes/*.java` (`RouterFunction`) → `api/handlers/*.java` (`@Component` handlers). Add/change endpoints in routers, not new `@RestController`s.
- All routes sit behind base path `spring.webflux.base-path: /franchise_system/api` (see `application.yml`) — README endpoint table omits the prefix.
- Clean-ish layering: `api` (handlers/routes/dtos), `infrastructure` (services, `abstract_services`, `helpers`, `utils`), `domain` (repositories, table entities, exceptions), `config`.
- Raw SQL lives in `infrastructure/services` and `infrastructure/helpers` via `DatabaseClient` with named params (e.g. stock update, max-stock analytics in `FranchiseService`). Annotation-driven R2DBC repositories (`R2dbcRepository`) are used elsewhere.
- Path IDs are validated manually: call `IdValidator.validate(id, "ResourceName")` in handlers → `IdMalformedException` (400).
- Errors are centralized in `api/handlers/ErrorHandler.java` (`ErrorWebExceptionHandler`): `ValidationException`/`IdMalformedException`/`ExistingResourceException`/`ServerWebInputException` → 400, `ResourceNotFoundException` → 404, else 500.
- Boot 4: Jackson imports are `tools.jackson.databind.*` (not `com.fasterxml.jackson`), Lombok is enabled, Java 21.

## DB / schema gotchas (`db/01-schema.sql`)
- Tables: `franchises`, `branches`, `products`, and junction `branch_products` (branch_id, product_id, product_stock). `branch_products` has **no primary key** and `product_stock` is **nullable**.
- Franchise/branch/product names are `UNIQUE NOT NULL` (`findByNameIgnoreCase` used to detect duplicates → 400).
- `franchises.branch_name`/`products.product_name`/`franchises.franchise_name` columns map via Lombok `@Table`/`@Column` entities in `domain/tables`.
- Init SQL only runs on **first** creation of the `mysql-data` volume. After editing `db/*.sql`, reset with `docker compose down -v` then `up -d`.

## Conventions
- Git: everything on `main`; commit messages use conventional-style prefixes (`feat:`, `refactor:`, `docs:`, `ci:`, `fix:`).
- Repo lives under a OneDrive path with spaces — always quote paths in shell commands.