package com.alejogiraldoo.franchisesystem.domain.tables;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "franchises")
public class FranchiseTable {

    @Id
    @Column(value="franchise_id")
    private Integer id;

    @Column(value="franchise_name")
    private String name;

}
