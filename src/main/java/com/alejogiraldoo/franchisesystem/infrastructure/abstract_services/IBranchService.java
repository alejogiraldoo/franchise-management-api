package com.alejogiraldoo.franchisesystem.infrastructure.abstract_services;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.BranchRequest;
import com.alejogiraldoo.franchisesystem.domain.tables.BranchTable;

public interface IBranchService extends IResourceCrudService<BranchRequest, BranchTable, Integer, Integer> {}
