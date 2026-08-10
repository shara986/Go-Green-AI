package com.gogreen.ai.repository;

import com.gogreen.ai.entity.Consultation;
import com.gogreen.ai.entity.enums.ConsultationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {

    List<Consultation> findByUserId(UUID userId);

    List<Consultation> findByExpertId(UUID expertId);

    List<Consultation> findByStatus(ConsultationStatus status);
}
