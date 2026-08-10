package com.gogreen.ai.repository;

import com.gogreen.ai.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnnouncementRepository extends JpaRepository<Announcement, UUID> {

    List<Announcement> findByActiveTrue();

    Page<Announcement> findByActiveTrue(Pageable pageable);

    Page<Announcement> findByTitleContainingIgnoreCase(String search, Pageable pageable);
}
