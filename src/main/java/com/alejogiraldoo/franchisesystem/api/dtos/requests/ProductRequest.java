package com.alejogiraldoo.franchisesystem.api.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotNull(message = "Product name is required")
    @Size(max = 50)
    private String name;

    @Min(value = 0, message = "Product stock must be a valid whole number starting from 0")
    private Integer stock;
}
