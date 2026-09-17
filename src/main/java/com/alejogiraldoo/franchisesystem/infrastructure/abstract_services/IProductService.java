package com.alejogiraldoo.franchisesystem.infrastructure.abstract_services;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.ProductRequest;
import com.alejogiraldoo.franchisesystem.api.dtos.responses.ProductStock;
import com.alejogiraldoo.franchisesystem.domain.tables.ProductTable;
import reactor.core.publisher.Mono;

public interface IProductService {

    Mono<Void> delete( Integer id, Integer branchId );
    Mono<ProductStock> create(ProductRequest request, Integer branchId);
    Mono<ProductTable> update(ProductRequest request, Integer productId);
}
