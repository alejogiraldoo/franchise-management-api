package com.alejogiraldoo.franchisesystem.domain.exceptions;

public class ExistingResourceException extends RuntimeException {
    public ExistingResourceException(String resource) {
        super(String.format("%s already exists", resource));
    }
}
