package com.alejogiraldoo.franchisesystem.infrastructure.services;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.ProductStockRequest;
import com.alejogiraldoo.franchisesystem.api.dtos.responses.ProductStock;
import com.alejogiraldoo.franchisesystem.domain.exceptions.ResourceNotFoundException;
import com.alejogiraldoo.franchisesystem.infrastructure.abstract_services.IStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockService implements IStockService {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<ProductStock> update(ProductStockRequest request, Integer branchId, Integer productId) {
        return this.databaseClient.sql(SELECT_BRANCH_PRODUCTS)
                .bind("branchId", branchId)
                .bind("productId", productId)
                .mapProperties( ProductStock.class )
                .all()
                .switchIfEmpty(
                        Mono.error(
                                new ResourceNotFoundException(String.format("Product with ID %s or Branch with ID %s", productId, branchId))
                        )
                )
                .flatMap( productStock -> {
                            productStock.setStock(request.getStock() );
                            return this.databaseClient.sql(UPDATE_PRODUCT_STOCK)
                                    .bind("productStock", request.getStock())
                                    .bind("branchId", branchId)
                                    .bind("productId", productId)
                                    .fetch()
                                    .all()
                                    .then( Mono.just( productStock ) );
                        }
                )
                .single()
                .doOnSuccess( productStock -> log.info("Product stock successfully updated: {}", productStock) )
                .doOnError( error -> log.error("Product stock couldn't be updated: ", error));
    }

    private static final String SELECT_BRANCH_PRODUCTS = """
            SELECT
            b.branch_name,
            p.product_name,
            bp.product_stock AS stock
            FROM branch_products bp
            INNER JOIN products p ON p.product_id = bp.product_id
            INNER JOIN branches b ON b.branch_id = bp.branch_id
            WHERE bp.branch_id = :branchId AND bp.product_id = :productId;
            """;

    private static final String UPDATE_PRODUCT_STOCK = """
            UPDATE branch_products
            SET product_stock = :productStock
            WHERE branch_id = :branchId AND product_id = :productId;
            """;
}
