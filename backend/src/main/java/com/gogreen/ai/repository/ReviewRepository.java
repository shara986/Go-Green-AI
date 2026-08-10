package com.gogreen.ai.repository;

import com.gogreen.ai.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByPlantId(UUID plantId);

    List<Review> findByUserId(UUID userId);

    Optional<Review> findByUserIdAndPlantId(UUID userId, UUID plantId);

    @Query("select r from Review r where " +
            "( :search is null or lower(r.comment) like lower(concat('%', :search, '%')) " +
            "   or lower(r.user.name) like lower(concat('%', :search, '%')) " +
            "   or lower(r.plant.name) like lower(concat('%', :search, '%')) )")
    Page<Review> searchReviews(@Param("search") String search, Pageable pageable);
}
