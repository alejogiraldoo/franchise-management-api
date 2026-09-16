package com.alejogiraldoo.franchisesystem.infrastructure.abstract_services;

import reactor.core.publisher.Mono;

public interface IResourceCrudService<RQ, RS, ID, RID> {
    Mono<RS> create( RQ request, RID resourceId );

    Mono<RS> update( RQ request, ID id );
}
