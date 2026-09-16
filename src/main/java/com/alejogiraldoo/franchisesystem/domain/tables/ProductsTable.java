package com.alejogiraldoo.franchisesystem.domain.tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class ProductsTable {

    @Id
    @Column(value="product_id")
    private Integer id;

    @Column(value="product_name")
    private String name;

}
