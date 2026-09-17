package com.alejogiraldoo.franchisesystem.api.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductStockRequest {

    @NotNull(message = "Product stock is required")
    private Integer stock;
}
