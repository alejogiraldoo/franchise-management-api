# Franchise Management API

A robust, reactive RESTful API designed to manage a hierarchical structure of franchises, branches, and product inventories. This project was developed as a Backend Developer Practical Test, focusing on clean architecture, reactive programming, and modern deployment practices.

## Core Features

- **Hierarchical Management**: Easily manage Franchises, their respective Branches, and the Products offered at each branch.
- **Inventory Control**: Add, remove, and update the stock of products in real-time.
- **Data Analytics**: Specialized endpoint to retrieve the product with the highest stock level for each branch within a specific franchise.
- **Reactive Architecture (Bonus)**: Built using **Spring WebFlux** and Project Reactor for non-blocking, highly concurrent execution.
- **Extended CRUD (Bonus)**: Dedicated endpoints to dynamically update the names of franchises, branches, and products.
- **Fully Dockerized (Bonus)**: The application and its database are containerized for seamless local deployment.
- **Cloud & IaC Ready (Bonus)**: Configured for cloud deployment with Infrastructure as Code (IaC) considerations.

## Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot (WebFlux)
- **Database**: MySQL
- **Persistence**: Spring Data R2DBC
- **Containerization**: Docker & Docker Compose
- **Infrastructure as Code**: Terraform
- **Version Control**: Git & GitHub (Feature Branch Workflow)

## Data Model

The following Entity-Relationship (ER) diagram illustrates the database architecture, highlighting the relationships between Franchises, Branches, and Products:

![Entity Relationship Diagram](./docs/FranchiseSystem.svg)

The database architecture follows a strict hierarchical model designed for high performance and straightforward data retrieval. As shown in the `FranchiseSystem.svg` diagram, the following design decisions were implemented:

* **Hierarchical Franchise Hierarchy:** The `branches` entity links to `franchises` via `franchise_id` (FK), enforcing a clear 1:N relationship where each branch belongs exclusively to one parent franchise.
* **Decoupled Product Catalog:** The `products` table maintains unique global product definitions (`product_id`, `name`), keeping the core catalog separate from operational branch data.
* **Normalized Inventory Junction (`branch_products`):** The M:N relationship between `branches` and `products` is resolved through the `branch_products` associative table. Placing `stock` directly inside this intersection table guarantees that inventory levels are strictly scoped to a specific branch-product pair without polluting the global product entity.
* **Database-Level Integrity Constraints:** Unique (`UN`) constraints on entity names prevent duplicate records for franchises, branches, and products.

## API Endpoints Reference

### Franchises
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/franchises` | Add a new franchise |
| `PATCH` | `/franchises/{id}/name` | Update the name of a franchise *(Bonus)* |

### Branches
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/franchises/{franchiseId}/branches` | Add a new branch to a franchise |
| `PATCH` | `/branches/{id}/name` | Update the name of a branch *(Bonus)* |

### Products & Inventory
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/branches/{branchId}/products` | Add a new product to a branch |
| `DELETE` | `/branches/{branchId}/products/{productId}`| Remove a product from a branch |
| `PATCH` | `/products/{id}/stock` | Modify the stock quantity of a product |
| `PATCH` | `/products/{id}/name` | Update the name of a product *(Bonus)* |

### Analytics
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/franchises/{franchiseId}/max-stock` | Get the product with the most stock per branch for a specific franchise |

## Installation & Local Deployment

This project uses Docker to simplify local deployment. You do not need to install the database locally, only Docker and Java.

### 1. Prerequisites
- **Java 17+** (Java 21 recommended)
- **Docker & Docker Compose** installed.

### 2. Clone the Repository
```bash
git clone https://github.com/alejogiraldoo/franchise-management-api.git
cd franchise-management-api
```

### 3. Clone the Repository
Use the provided ```docker-compose.yml``` to start the database:
```bash
docker-compose up -d
```

### 4. Run the Application
You can run the application using the Maven Wrapper:
```bash
./mvnw spring-boot:run
```

(On Windows, use ```.\mvnw.cmd spring-boot:run```)

The server will start on http://localhost:8080.


#### Architecture & Best Practices
- **Git Workflow:** This project was built using a structured Git workflow (e.g., GitFlow or Feature Branches) to ensure clean commit history and traceability.

- **Reactive Streams:** Utilizes Mono and Flux to handle requests asynchronously, providing better resource utilization and scalability.

- **Separation of Concerns:** Strictly layered architecture separating Controllers (Handlers), Services (Business Logic), and Repositories (Data Access).

Developed as a practical assessment for a Backend Developer position.