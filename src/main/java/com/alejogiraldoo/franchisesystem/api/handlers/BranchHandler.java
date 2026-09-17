package com.alejogiraldoo.franchisesystem.api.handlers;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.BranchRequest;
import com.alejogiraldoo.franchisesystem.api.dtos.requests.ProductRequest;
import com.alejogiraldoo.franchisesystem.api.dtos.requests.ProductStockRequest;
import com.alejogiraldoo.franchisesystem.config.ReactiveValidatorConfig;
import com.alejogiraldoo.franchisesystem.infrastructure.services.BranchService;
import com.alejogiraldoo.franchisesystem.infrastructure.services.ProductService;
import com.alejogiraldoo.franchisesystem.infrastructure.services.StockService;
import com.alejogiraldoo.franchisesystem.infrastructure.utils.IdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BranchHandler {

    private final BranchService branchService;
    private final ProductService productService;
    private final StockService stockService;
    private final ReactiveValidatorConfig validator;

    public Mono<ServerResponse> updateBranch( ServerRequest request ) {
        Integer branchId =  IdValidator.validate(
                request.pathVariable("id"),
                "Branch"
        );

        return request.bodyToMono(BranchRequest.class)
                .flatMap(this.validator::validate)
                .flatMap( body ->
                        this.branchService.update( body, branchId )
                                .flatMap( updatedBranch ->
                                        ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue( updatedBranch )
                                )
                );
    }

    public Mono<ServerResponse> addProduct( ServerRequest request ) {
        Integer branchId =  IdValidator.validate(
                request.pathVariable("branchId"),
                "Branch"
        );

        return request.bodyToMono(ProductRequest.class)
                .flatMap(this.validator::validate)
                .flatMap( body ->
                        this.productService.create( body, branchId )
                                .flatMap( newProduct ->
                                        ServerResponse
                                                .status(HttpStatus.CREATED)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue( newProduct )
                                )
                );
    }

    public Mono<ServerResponse> removeProduct( ServerRequest request ) {
        Integer branchId =  IdValidator.validate(
                request.pathVariable("branchId"),
                "Branch"
        );

        Integer productId =  IdValidator.validate(
                request.pathVariable("productId"),
                "Product"
        );

        return this.productService.delete( productId, branchId )
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateProductStock( ServerRequest request ) {
        Integer branchId =  IdValidator.validate(
                request.pathVariable("branchId"),
                "Branch"
        );

        Integer productId =  IdValidator.validate(
                request.pathVariable("productId"),
                "Product"
        );

        return request.bodyToMono(ProductStockRequest.class)
                .flatMap(this.validator::validate)
                .flatMap( body ->
                        this.stockService.update( body, branchId, productId )
                                .flatMap( productStock ->
                                        ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue( productStock )
                                )
                );
    }

}
