package com.alejogiraldoo.franchisesystem.infrastructure.services;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.ProductStockRequest;
import com.alejogiraldoo.franchisesystem.api.dtos.responses.ProductStock;
import com.alejogiraldoo.franchisesystem.infrastructure.abstract_services.IStockService;
import com.alejogiraldoo.franchisesystem.infrastructure.helpers.ProductHelper;
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
    private final ProductHelper productHelper;

    @Override
    public Mono<ProductStock> update(ProductStockRequest request, Integer branchId, Integer productId) {
        return this.productHelper.getProductStockInfo( branchId, productId )
                .flatMap( productStock -> {
                            productStock.setStock(request.getStock() );
                            return this.databaseClient.sql(UPDATE_PRODUCT_STOCK)
                                    .bind("productStock", request.getStock())
                                    .bind("branchId", branchId)
                                    .bind("productId", productId)
                                    .fetch()
                                    .one()
                                    .then( Mono.just( productStock ) );
                        }
                )
                .single()
                .doOnSuccess( productStock -> log.info("Product stock successfully updated: {}", productStock) )
                .doOnError( error -> log.error("Product stock couldn't be updated: ", error));
    }

    private static final String UPDATE_PRODUCT_STOCK = """
            UPDATE branch_products
            SET product_stock = :productStock
            WHERE branch_id = :branchId AND product_id = :productId;
            """;
}
