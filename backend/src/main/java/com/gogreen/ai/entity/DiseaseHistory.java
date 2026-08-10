package com.gogreen.ai.entity;

import com.gogreen.ai.entity.enums.DiseaseSeverity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "disease_histories", indexes = {
        @Index(name = "idx_disease_histories_user_id", columnList = "user_id"),
        @Index(name = "idx_disease_histories_date", columnList = "date_identified")
})
public class DiseaseHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Size(max = 150)
    @Column(name = "plant_name", nullable = false, length = 150)
    private String plantName;

    @NotBlank
    @Size(max = 150)
    @Column(name = "disease_identified", nullable = false, length = 150)
    private String diseaseIdentified;

    @NotNull
    @Column(name = "date_identified", nullable = false)
    private LocalDate dateIdentified;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DiseaseSeverity severity;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Size(max = 2000)
    @Column(name = "recommended_action", length = 2000)
    private String recommendedAction;

    @Size(max = 500)
    @Column(name = "image_url", length = 500)
    private String imageUrl;
}
