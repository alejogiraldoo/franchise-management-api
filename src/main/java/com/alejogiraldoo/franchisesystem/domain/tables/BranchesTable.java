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
@Table(name = "branches")
public class BranchesTable {

    @Id
    @Column(value="branch_id")
    private Integer id;

    @Column(value="branch_name")
    private String name;

    @Column(value="franchise_id")
    private Integer franchiseId;

}
