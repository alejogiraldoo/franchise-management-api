package com.alejogiraldoo.franchisesystem.api.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductStockRequest {

    @NotNull(message = "Product stock is required")
    @Positive( message = "Product stock must be a valid whole number starting from 0")
    private Integer stock;
}
