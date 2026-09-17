package com.alejogiraldoo.franchisesystem.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReactiveValidatorConfig {

    private final Validator validator;

    public <T> Mono<T> validate( T object ){
        final var violationErrors = validator.validate(object);

        if( violationErrors.isEmpty() ){
            return Mono.just(object);
        }

        final var errors = violationErrors.stream()
                .map( ConstraintViolation::getMessage )
                .collect(Collectors.joining(", "));

        return Mono.error(new ValidationException(errors));
    }
}
