package com.gogreen.ai.repository;

import com.gogreen.ai.entity.PlantDiaryEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlantDiaryEntryRepository extends JpaRepository<PlantDiaryEntry, UUID> {

    List<PlantDiaryEntry> findByDiaryId(UUID diaryId);
}
