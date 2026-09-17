package com.alejogiraldoo.franchisesystem.infrastructure.services;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.ProductRequest;
import com.alejogiraldoo.franchisesystem.domain.repositories.BranchRepository;
import com.alejogiraldoo.franchisesystem.domain.repositories.ProductRepository;
import com.alejogiraldoo.franchisesystem.domain.tables.ProductTable;
import com.alejogiraldoo.franchisesystem.infrastructure.abstract_services.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final DatabaseClient databaseClient;

    @Override
    public Mono<Void> delete(Integer id, Integer branchId) {
                return this.databaseClient.sql(SELECT_PRODUCT_FROM_BRANCH)
                        .bind("branchId", branchId)
                        .bind("productId", id)
                        .fetch()
                        .one()
                        .filter( result -> (Long) result.get("product_exists") == 1)
                        .hasElement()
                        .flatMap( exists -> {
                            if (!exists) return
                                    Mono.error(new IllegalArgumentException("Not found Product in Branch"));

                            return this.databaseClient.sql(DELETE_PRODUCT_FROM_BRANCH)
                                    .bind("branchId", branchId)
                                    .bind("productId", id)
                                    .fetch()
                                    .rowsUpdated();
                        })
                        .then()
                        .doOnSuccess( result -> log.info("Product successfully removed from branch: ID {}", id) )
                        .doOnError( error -> log.error("Product couldn't be removed from branch: ", error));
    }

    @Override
    public Mono<ProductTable> create(ProductRequest request, Integer branchId) {
                return Flux.zip(
                        this.productRepository.findByNameIgnoreCase( request.getName() )
                                .hasElement(),
                        this.branchRepository.findById( branchId )
                                .hasElement()
                )
                .flatMap( tuple -> {
                    if ( tuple.getT1() ) return
                            Mono.error(new IllegalArgumentException("Product already exists"));

                    if ( !tuple.getT2() ) return
                            Mono.error(new IllegalArgumentException("Not found Branch"));

                    var newProduct = ProductTable.builder()
                            .name( request.getName() )
                            .build();

                    return this.productRepository.save( newProduct )
                            .flatMap( product ->
                                this.databaseClient.sql(INSERT_BRANCH_PRODUCTS)
                                        .bind("branchId", branchId)
                                        .bind("productId", product.getId())
                                        .bind("productStock", Optional.ofNullable(request.getStock()).orElse(0)  )
                                        .fetch()
                                        .rowsUpdated()
                                        .then( Mono.just( product ) )
                            )
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .single()
                .doOnSuccess( product -> log.info("Product successfully created: {}", product) )
                .doOnError( error -> log.error("Product couldn't be created: ", error));
    }

    @Override
    public Mono<ProductTable> update(ProductRequest request, Integer id) {
        return this.productRepository.findById( id )
                .switchIfEmpty( Mono.error(new IllegalArgumentException("Not found Product")) )
                .flatMap( product -> {

                    product.setName( request.getName() );

                    return this.productRepository.save( product )
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .doOnSuccess( product -> log.info("Product successfully updated: {}", product) )
                .doOnError( error -> log.error("Product couldn't be updated: ", error));
    }

    private static final String SELECT_PRODUCT_FROM_BRANCH = """
            SELECT COUNT(*) = 1 AS product_exists FROM branch_products WHERE branch_id = :branchId AND product_id = :productId;
            """;

    private static final String DELETE_PRODUCT_FROM_BRANCH = """
            DELETE FROM branch_products WHERE branch_id = :branchId AND product_id = :productId;
            """;

    private static final String INSERT_BRANCH_PRODUCTS = """
            INSERT INTO branch_products (branch_id, product_id, product_stock)
            VALUES(:branchId, :productId, :productStock);
            """;
}
