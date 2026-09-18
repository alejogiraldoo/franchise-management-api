package com.alejogiraldoo.franchisesystem.api.handlers;

import com.alejogiraldoo.franchisesystem.domain.exceptions.ExistingResourceException;
import com.alejogiraldoo.franchisesystem.domain.exceptions.IdMalformedException;
import com.alejogiraldoo.franchisesystem.domain.exceptions.ResourceNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        log.info("User Request Validation error: {}", ex.getMessage());

        HttpStatus status;
        String message = ex.getMessage();;

        if(        ex instanceof ValidationException
                || ex instanceof IdMalformedException
                || ex instanceof ExistingResourceException
        ){
            status = HttpStatus.BAD_REQUEST;
        } else if ( ex instanceof ServerWebInputException ) {
            status = HttpStatus.BAD_REQUEST;
            message = "Invalid request format, please confirm attributes types";
        } else if ( ex instanceof ResourceNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        }else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("timestampt", LocalDateTime.now().toString());
        errorResponse.put("path", exchange.getRequest().getPath().value());
        errorResponse.put( "status", status.value() );
        errorResponse.put( "error", status.getReasonPhrase() );
        errorResponse.put( "message", message );

        exchange.getResponse().setStatusCode( status );
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            final var bytes = objectMapper.writeValueAsBytes(errorResponse);
            final var buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch ( Exception e ) {
            log.error("An error occurred while sending request with errors", e);
            return Mono.error(e);
        }

    }
}
