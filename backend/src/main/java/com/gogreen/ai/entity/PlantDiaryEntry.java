package com.gogreen.ai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "plant_diary_entries", indexes = {
        @Index(name = "idx_plant_diary_entries_diary_id", columnList = "diary_id"),
        @Index(name = "idx_plant_diary_entries_entry_date", columnList = "entry_date")
})
public class PlantDiaryEntry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private PlantDiary diary;

    @NotNull
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Size(max = 2000)
    @Column(length = 2000)
    private String note;

    @Size(max = 500)
    @Column(name = "photo_url", length = 500)
    private String photoUrl;
}
