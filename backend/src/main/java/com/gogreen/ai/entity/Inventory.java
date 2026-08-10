package com.gogreen.ai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventories", indexes = {
        @Index(name = "idx_inventories_plant_id", columnList = "plant_id")
})
public class Inventory extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false, unique = true)
    private Plant plant;

    @Min(0)
    @Column(name = "stock_level", nullable = false)
    private Integer stockLevel;

    @Min(0)
    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity = 0;

    @Min(0)
    @Column(name = "reorder_level", nullable = false)
    private Integer reorderLevel = 10;

    @Column(name = "last_restock_date")
    private LocalDateTime lastRestockDate;
}
