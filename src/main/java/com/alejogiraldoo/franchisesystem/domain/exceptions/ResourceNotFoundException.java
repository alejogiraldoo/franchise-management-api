package com.alejogiraldoo.franchisesystem.domain.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource) {
        super(String.format("Not found %s", resource));
    }
}
