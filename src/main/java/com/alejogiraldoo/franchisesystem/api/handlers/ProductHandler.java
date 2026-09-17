package com.alejogiraldoo.franchisesystem.api.handlers;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.ProductRequest;
import com.alejogiraldoo.franchisesystem.config.ReactiveValidatorConfig;
import com.alejogiraldoo.franchisesystem.infrastructure.services.ProductService;
import com.alejogiraldoo.franchisesystem.infrastructure.utils.IdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final ProductService productService;
    private final ReactiveValidatorConfig validator;

    public Mono<ServerResponse> updateProduct(ServerRequest request ) {
        Integer productId =  IdValidator.validate(
                request.pathVariable("id"),
                "Product"
        );

        return request.bodyToMono(ProductRequest.class)
                .flatMap(this.validator::validate)
                .flatMap( body ->
                        this.productService.update( body, productId )
                                .flatMap( updatedProduct ->
                                        ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue( updatedProduct )
                                )
                );
    }
}
