package com.alejogiraldoo.franchisesystem.api.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FranchiseRequest {

    private String name;
}
