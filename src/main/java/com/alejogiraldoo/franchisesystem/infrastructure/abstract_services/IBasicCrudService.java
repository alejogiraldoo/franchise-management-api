package com.alejogiraldoo.franchisesystem.infrastructure.abstract_services;

import reactor.core.publisher.Mono;

public interface IBasicCrudService<RQ, RS, ID> {
    Mono<RS> create( RQ request );

    Mono<RS> update( RQ request, ID id );
}
