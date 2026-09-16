package com.alejogiraldoo.franchisesystem.domain.repositories;

import com.alejogiraldoo.franchisesystem.domain.tables.ProductTable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends R2dbcRepository<ProductTable, Integer> {
    Mono<ProductTable> findByNameIgnoreCase(String name);
}
