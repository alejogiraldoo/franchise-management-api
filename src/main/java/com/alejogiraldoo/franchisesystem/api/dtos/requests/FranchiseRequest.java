package com.alejogiraldoo.franchisesystem.api.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FranchiseRequest {

    @NotNull(message = "Franchise name is required")
    @Size(max = 50)
    private String name;
}
