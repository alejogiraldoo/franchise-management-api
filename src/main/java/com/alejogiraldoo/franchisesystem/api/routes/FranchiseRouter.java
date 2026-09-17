package com.alejogiraldoo.franchisesystem.api.routes;

import com.alejogiraldoo.franchisesystem.api.handlers.FranchiseHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FranchiseRouter {

    @Bean
    public RouterFunction<ServerResponse> franchiseRoutes( FranchiseHandler franchiseHandler ) {
        return route()
                .GET( MAX_STOCK_URL, franchiseHandler::getProductWithMostStock )
                .POST( BASE_URL, franchiseHandler::createFranchise )
                .POST( NEW_BRANCH_URL, franchiseHandler::addBranch )
                .PATCH(UPDATE_NAME_URL, franchiseHandler::updateFranchise )
                .build();
    }

    private final static String BASE_URL = "/franchises";
    private final static String UPDATE_NAME_URL = BASE_URL + "/{id}/name";
    private final static String MAX_STOCK_URL = BASE_URL + "/{franchiseId}/max-stock";
    private final static String NEW_BRANCH_URL = BASE_URL + "/{franchiseId}/branches";
}
