package com.alejogiraldoo.franchisesystem.infrastructure.services;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.BranchRequest;
import com.alejogiraldoo.franchisesystem.domain.exceptions.ExistingResourceException;
import com.alejogiraldoo.franchisesystem.domain.exceptions.ResourceNotFoundException;
import com.alejogiraldoo.franchisesystem.domain.repositories.BranchRepository;
import com.alejogiraldoo.franchisesystem.domain.repositories.FranchiseRepository;
import com.alejogiraldoo.franchisesystem.domain.tables.BranchTable;
import com.alejogiraldoo.franchisesystem.infrastructure.abstract_services.IBranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Service
public class BranchService implements IBranchService {

    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    @Override
    public Mono<BranchTable> create(BranchRequest request, Integer franchiseId) {
        return Flux.zip(
                this.branchRepository.findByNameIgnoreCase( request.getName() )
                        .hasElement(),
                this.franchiseRepository.findById( franchiseId )
                        .hasElement()
        )
                .flatMap( tuple -> {
                    if ( tuple.getT1() ) return
                            Mono.error(
                                    new ExistingResourceException(String.format("Branch %s", request.getName()))
                            );

                    if ( !tuple.getT2() ) return
                            Mono.error(
                                    new ResourceNotFoundException(String.format("Franchise with ID: %s", franchiseId))
                            );

                    var newBranch = BranchTable.builder()
                            .name( request.getName() )
                            .franchiseId( franchiseId )
                            .build();

                    return this.branchRepository.save( newBranch )
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .single()
                .doOnSuccess( branch -> log.info("Branch successfully created: {}", branch) )
                .doOnError( error -> log.error("Branch couldn't be created: ", error));
    }

    @Override
    public Mono<BranchTable> update(BranchRequest request, Integer id) {
        return this.branchRepository.findById( id )
                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(String.format("Branch with ID: %s", id))
                        )
                )
                .flatMap( branch -> {

                    branch.setName( request.getName() );

                    return this.branchRepository.save( branch )
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .doOnSuccess( branch -> log.info("Branch successfully updated: {}", branch) )
                .doOnError( error -> log.error("Branch couldn't be updated: ", error));
    }

}
