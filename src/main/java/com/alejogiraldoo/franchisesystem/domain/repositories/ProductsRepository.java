package com.alejogiraldoo.franchisesystem.domain.repositories;

import com.alejogiraldoo.franchisesystem.domain.tables.ProductsTable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ProductsRepository extends R2dbcRepository<ProductsTable, Integer> {
}
