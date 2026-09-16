package com.alejogiraldoo.franchisesystem.domain.repositories;

import com.alejogiraldoo.franchisesystem.domain.tables.BranchesTable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface BranchesRepository extends R2dbcRepository<BranchesTable, Integer> {
}
