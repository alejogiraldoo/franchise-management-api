package com.alejogiraldoo.franchisesystem.infrastructure.helpers;

import com.alejogiraldoo.franchisesystem.api.dtos.responses.ProductStock;
import com.alejogiraldoo.franchisesystem.domain.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductHelper {

    private final DatabaseClient databaseClient;

    public Mono<ProductStock> getProductStockInfo(Integer branchId, Integer productId) {
        return this.databaseClient.sql(SELECT_BRANCH_PRODUCTS)
                .bind("branchId", branchId)
                .bind("productId", productId)
                .mapProperties( ProductStock.class )
                .one()
                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(String.format("Product with ID %s or Branch with ID %s", productId, branchId))
                        )
                )
                .doOnSuccess( productStock -> log.info("Product info successfully retrieved: {}", productStock) )
                .doOnError( error -> log.error("Product info couldn't be retrieved: ", error));
    }

    private static final String SELECT_BRANCH_PRODUCTS = """
            SELECT
            p.product_id,
            p.product_name,
            b.branch_name,
            bp.product_stock AS stock
            FROM branch_products bp
            INNER JOIN products p ON p.product_id = bp.product_id
            INNER JOIN branches b ON b.branch_id = bp.branch_id
            WHERE bp.branch_id = :branchId AND bp.product_id = :productId;
            """;
}
