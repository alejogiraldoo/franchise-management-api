package com.alejogiraldoo.franchisesystem.api.routes;

import com.alejogiraldoo.franchisesystem.api.handlers.BranchHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BranchRouter {

    @Bean
    public RouterFunction<ServerResponse> branchRoutes( BranchHandler branchHandler ) {
        return route()
                .POST( NEW_PRODUCT_URL, branchHandler::addProduct )
                .PATCH(UPDATE_NAME_URL, branchHandler::updateBranch )
                .PATCH( UPDATE_PRODUCT_STOCK_URL, branchHandler::updateProductStock )
                .DELETE( REMOVE_PRODUCT_URL, branchHandler::removeProduct )
                .build();
    }

    private final static String BASE_URL = "/branches";
    private final static String UPDATE_NAME_URL = BASE_URL + "/{id}/name";
    private final static String NEW_PRODUCT_URL = BASE_URL + "/{branchId}/products";
    private final static String REMOVE_PRODUCT_URL = BASE_URL + "/{branchId}/products/{productId}";
    private final static String UPDATE_PRODUCT_STOCK_URL = BASE_URL + "/{branchId}/products/{productId}/stock";

}
