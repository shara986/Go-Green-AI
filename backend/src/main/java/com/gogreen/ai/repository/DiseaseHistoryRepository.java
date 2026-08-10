package com.gogreen.ai.repository;

import com.gogreen.ai.entity.DiseaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DiseaseHistoryRepository extends JpaRepository<DiseaseHistory, UUID> {

    List<DiseaseHistory> findByUserId(UUID userId);
}
