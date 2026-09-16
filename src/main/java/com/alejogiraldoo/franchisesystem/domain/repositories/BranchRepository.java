package com.alejogiraldoo.franchisesystem.domain.repositories;

import com.alejogiraldoo.franchisesystem.domain.tables.BranchTable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface BranchRepository extends R2dbcRepository<BranchTable, Integer> {
    Mono<BranchTable> findByNameIgnoreCase(String name);
}
