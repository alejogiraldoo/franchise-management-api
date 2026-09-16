package com.alejogiraldoo.franchisesystem.infrastructure.abstract_services;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.ProductStockRequest;
import com.alejogiraldoo.franchisesystem.api.dtos.responses.ProductStock;
import reactor.core.publisher.Mono;

public interface IStockService {

    Mono<ProductStock> update(ProductStockRequest request, Integer branchId, Integer productId);
}
