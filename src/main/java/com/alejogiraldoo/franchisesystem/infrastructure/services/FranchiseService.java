package com.alejogiraldoo.franchisesystem.infrastructure.services;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.FranchiseRequest;
import com.alejogiraldoo.franchisesystem.api.dtos.responses.ProductWithMostStock;
import com.alejogiraldoo.franchisesystem.domain.exceptions.ExistingResourceException;
import com.alejogiraldoo.franchisesystem.domain.exceptions.ResourceNotFoundException;
import com.alejogiraldoo.franchisesystem.domain.repositories.FranchiseRepository;
import com.alejogiraldoo.franchisesystem.domain.tables.FranchiseTable;
import com.alejogiraldoo.franchisesystem.infrastructure.abstract_services.IFranchiseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Service
public class FranchiseService implements IFranchiseService {

    private final DatabaseClient databaseClient;
    private final FranchiseRepository franchiseRepository;

    @Override
    public Flux<ProductWithMostStock> getProductWithMostStock(Integer franchiseId) {
        return this.databaseClient.sql(SELECT_PRODUCT_WITH_MOST_STOCK_PER_BRANCH)
                .bind("franchiseId", franchiseId)
                .mapProperties( ProductWithMostStock.class )
                .all()
                .doOnSubscribe( subscription -> log.info("Retrieving franchise products with most stock per branch: ") )
                .doOnNext( product -> log.info("Product: {}", product))
                .doOnError( error -> log.error("Franchise couldn't be created: ", error));
    }

    @Override
    public Mono<FranchiseTable> create(FranchiseRequest request) {
        return this.franchiseRepository.findByNameIgnoreCase( request.getName() )
                .hasElement()
                .flatMap( exists -> {
                    if ( exists ) return
                            Mono.error(
                                    new ExistingResourceException(String.format("Franchise %s", request.getName()))
                            );

                    var newFranchise = FranchiseTable.builder()
                            .name( request.getName() )
                            .build();

                    return this.franchiseRepository.save( newFranchise )
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .doOnSuccess( franchise -> log.info("Franchise successfully created: {}", franchise) )
                .doOnError( error -> log.error("Franchise couldn't be created: ", error));

    }

    @Override
    public Mono<FranchiseTable> update(FranchiseRequest request, Integer id) {
        return this.franchiseRepository.findById( id )
                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(String.format("Franchise with ID: %s", id))
                        )
                )
                .flatMap( franchise -> {

                    franchise.setName( request.getName() );

                    return this.franchiseRepository.save( franchise )
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .doOnSuccess( franchise -> log.info("Franchise successfully updated: {}", franchise) )
                .doOnError( error -> log.error("Franchise couldn't be updated: ", error));

    }

    private static final String SELECT_PRODUCT_WITH_MOST_STOCK_PER_BRANCH = """
            SELECT
            	branch_name,
            	p.product_name,
            	max_stock
            FROM (
            	SELECT
            	b.branch_name,
            	MAX( bp.product_stock ) AS max_stock\s
            	FROM products p
            	INNER JOIN branch_products bp ON bp.product_id = p.product_id
            	INNER JOIN branches b ON b.branch_id = bp.branch_id
            	WHERE b.franchise_id = :franchiseId
            	GROUP BY b.branch_name
            ) AS branch_max_stock
            INNER JOIN branch_products bp ON bp.product_stock = max_stock
            INNER JOIN products p ON p.product_id = bp.product_id;
            """;
}
