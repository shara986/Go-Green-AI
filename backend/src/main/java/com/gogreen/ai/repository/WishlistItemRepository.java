package com.gogreen.ai.repository;

import com.gogreen.ai.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, UUID> {

    List<WishlistItem> findByWishlistId(UUID wishlistId);

    Optional<WishlistItem> findByWishlistIdAndPlantId(UUID wishlistId, UUID plantId);
}
