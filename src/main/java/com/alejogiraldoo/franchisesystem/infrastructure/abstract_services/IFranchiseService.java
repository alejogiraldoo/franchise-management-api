package com.alejogiraldoo.franchisesystem.infrastructure.abstract_services;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.FranchiseRequest;
import com.alejogiraldoo.franchisesystem.api.dtos.responses.ProductWithMostStock;
import com.alejogiraldoo.franchisesystem.domain.tables.FranchiseTable;
import reactor.core.publisher.Flux;

public interface IFranchiseService extends IBasicCrudService<FranchiseRequest, FranchiseTable, Integer> {

    Flux<ProductWithMostStock> getProductWithMostStock( Integer franchiseId );

}
