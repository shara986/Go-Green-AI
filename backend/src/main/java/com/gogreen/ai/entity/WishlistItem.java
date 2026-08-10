package com.gogreen.ai.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
        name = "wishlist_items",
        indexes = {
                @Index(name = "idx_wishlist_items_wishlist_id", columnList = "wishlist_id"),
                @Index(name = "idx_wishlist_items_plant_id", columnList = "plant_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_wishlist_items_wishlist_plant", columnNames = {"wishlist_id", "plant_id"})
        }
)
public class WishlistItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;
}
