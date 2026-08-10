package com.gogreen.ai.repository;

import com.gogreen.ai.entity.PlantDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlantDiaryRepository extends JpaRepository<PlantDiary, UUID> {

    List<PlantDiary> findByUserId(UUID userId);
}
