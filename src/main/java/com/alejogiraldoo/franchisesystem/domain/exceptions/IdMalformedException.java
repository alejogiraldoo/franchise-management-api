package com.alejogiraldoo.franchisesystem.domain.exceptions;

public class IdMalformedException extends RuntimeException {
    public IdMalformedException(String resource) {
        super(String.format("%s ID malformed", resource));
    }
}
