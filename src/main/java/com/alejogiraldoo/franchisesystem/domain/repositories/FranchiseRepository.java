package com.alejogiraldoo.franchisesystem.domain.repositories;

import com.alejogiraldoo.franchisesystem.domain.tables.FranchiseTable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface FranchiseRepository extends R2dbcRepository<FranchiseTable, Integer> {
    Mono<FranchiseTable> findByNameIgnoreCase(String name);
}
