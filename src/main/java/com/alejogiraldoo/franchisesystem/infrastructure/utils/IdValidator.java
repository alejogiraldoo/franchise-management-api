package com.alejogiraldoo.franchisesystem.infrastructure.utils;

import com.alejogiraldoo.franchisesystem.domain.exceptions.IdMalformedException;

public class IdValidator {

    public static Integer validate( String id, String resourceName ) throws IdMalformedException {
        try {
            return Integer.valueOf(id);
        } catch (NumberFormatException e) {
            throw new IdMalformedException(resourceName);
        }
    }

}
