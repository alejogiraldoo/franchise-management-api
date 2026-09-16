package com.alejogiraldoo.franchisesystem.infrastructure.abstract_services;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.ProductRequest;
import com.alejogiraldoo.franchisesystem.domain.tables.ProductTable;
import reactor.core.publisher.Mono;

public interface IProductService extends IResourceCrudService<ProductRequest, ProductTable, Integer, Integer> {

    Mono<Void> delete(Integer id );

}
