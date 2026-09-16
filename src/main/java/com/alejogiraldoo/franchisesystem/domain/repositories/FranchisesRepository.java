package com.alejogiraldoo.franchisesystem.domain.repositories;

import com.alejogiraldoo.franchisesystem.domain.tables.FranchisesTable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface FranchisesRepository extends R2dbcRepository<FranchisesTable, Integer> {
}
