package com.alejogiraldoo.franchisesystem.api.routes;

import com.alejogiraldoo.franchisesystem.api.handlers.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> productRoutes( ProductHandler productHandler ) {
        return route()
                .PATCH(UPDATE_NAME_URL, productHandler::updateProduct)
                .build();
    }

    private final static String BASE_URL = "/products";
    private final static String UPDATE_NAME_URL = BASE_URL + "/{id}/name";
}
