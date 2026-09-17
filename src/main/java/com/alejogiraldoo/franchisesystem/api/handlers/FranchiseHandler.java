package com.alejogiraldoo.franchisesystem.api.handlers;

import com.alejogiraldoo.franchisesystem.api.dtos.requests.BranchRequest;
import com.alejogiraldoo.franchisesystem.api.dtos.requests.FranchiseRequest;
import com.alejogiraldoo.franchisesystem.config.ReactiveValidatorConfig;
import com.alejogiraldoo.franchisesystem.infrastructure.services.BranchService;
import com.alejogiraldoo.franchisesystem.infrastructure.services.FranchiseService;
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
public class FranchiseHandler {

    private final FranchiseService franchiseService;
    private final BranchService branchService;
    private final ReactiveValidatorConfig validator;

    public Mono<ServerResponse> getProductWithMostStock( ServerRequest request ) {
        Integer franchiseId =  IdValidator.validate(
                request.pathVariable("franchiseId"),
                "Franchise"
        );

        return this.franchiseService.getProductWithMostStock( franchiseId )
                .collectList()
                .flatMap( productWithMostStock -> {
                    if( productWithMostStock.isEmpty() ) return ServerResponse.noContent().build();

                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue( productWithMostStock );
                });
    }

    public Mono<ServerResponse> createFranchise( ServerRequest request ) {
        return request.bodyToMono(FranchiseRequest.class)
                .flatMap(this.validator::validate)
                .flatMap( body ->
                        this.franchiseService.create( body )
                                .flatMap( newFranchise ->
                                        ServerResponse
                                                .status(HttpStatus.CREATED)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue( newFranchise )
                                )
                );
    }

    public Mono<ServerResponse> updateFranchise( ServerRequest request ) {
        Integer franchiseId =  IdValidator.validate(
                request.pathVariable("id"),
                "Franchise"
        );

        return request.bodyToMono(FranchiseRequest.class)
                .flatMap(this.validator::validate)
                .flatMap( body ->
                        this.franchiseService.update( body, franchiseId )
                                .flatMap( updatedFranchise ->
                                        ServerResponse.ok()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue( updatedFranchise )
                                )
                );
    }

    public Mono<ServerResponse> addBranch( ServerRequest request ) {
        Integer franchiseId =  IdValidator.validate(
                request.pathVariable("franchiseId"),
                "Franchise"
        );

        return request.bodyToMono(BranchRequest.class)
                .flatMap(this.validator::validate)
                .flatMap( body ->
                        this.branchService.create( body, franchiseId )
                                .flatMap( newBranch ->
                                        ServerResponse
                                                .status(HttpStatus.CREATED)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .bodyValue( newBranch )
                                )
                );
    }
}
