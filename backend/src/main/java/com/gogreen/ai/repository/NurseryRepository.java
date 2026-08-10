package com.gogreen.ai.repository;

import com.gogreen.ai.entity.Nursery;
import com.gogreen.ai.entity.enums.NurseryApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NurseryRepository extends JpaRepository<Nursery, UUID> {

    Optional<Nursery> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    long countByApprovalStatus(NurseryApprovalStatus approvalStatus);

    Page<Nursery> findByApprovalStatus(NurseryApprovalStatus approvalStatus, Pageable pageable);

    @Query("select n from Nursery n where " +
            "( :search is null or lower(n.name) like lower(concat('%', :search, '%')) " +
            "   or lower(n.city) like lower(concat('%', :search, '%')) " +
            "   or lower(n.contactEmail) like lower(concat('%', :search, '%')) )")
    Page<Nursery> searchNurseries(@org.springframework.data.repository.query.Param("search") String search, Pageable pageable);

    @Query("select n.name as nurseryName, count(o) as orderCount " +
            "from Nursery n join n.user u join Order o on o.user = u " +
            "where o.status <> 'CANCELLED' group by n.name order by orderCount desc")
    List<Object[]> findNurseryPerformance();
}
