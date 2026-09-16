package com.alejogiraldoo.franchisesystem.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductWithMostStock {

    private String branchName;
    private String productName;
    private Integer maxStock;
}
