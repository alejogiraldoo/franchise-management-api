package com.alejogiraldoo.franchisesystem.config;

import jakarta.annotation.PreDestroy;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;


@Configuration
public class ValidatorConfig {

    private ValidatorFactory validatorFactory;

    public Validator validator(){
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        return this.validatorFactory.getValidator();
    }

    @PreDestroy
    public void destroy(){
        if(Objects.nonNull(this.validatorFactory))
            this.validatorFactory.close();
    }

}
