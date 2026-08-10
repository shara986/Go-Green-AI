package com.gogreen.ai.entity;

import com.gogreen.ai.entity.enums.PlantType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "plants",
        indexes = {
                @Index(name = "idx_plants_nursery_id", columnList = "nursery_id"),
                @Index(name = "idx_plants_category_id", columnList = "category_id"),
                @Index(name = "idx_plants_name", columnList = "name"),
                @Index(name = "idx_plants_plant_type", columnList = "plant_type")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_plants_nursery_sku", columnNames = {"nursery_id", "sku"})
        }
)
public class Plant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nursery_id", nullable = false)
    private Nursery nursery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String name;

    @Size(max = 150)
    @Column(name = "scientific_name", length = 150)
    private String scientificName;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String sku;

    @Size(max = 2000)
    @Column(length = 2000)
    private String description;

    @Size(max = 2000)
    @Column(name = "care_instructions", length = 2000)
    private String careInstructions;

    @Positive
    @Column(nullable = false)
    private Double price;

    @Min(0)
    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "plant_type", nullable = false, length = 30)
    private PlantType plantType;

    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}
