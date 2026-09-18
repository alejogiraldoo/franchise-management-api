package com.alejogiraldoo.franchisesystem.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductStock {

    private Integer productId;
    private String productName;
    private String branchName;
    private Integer stock;
}
