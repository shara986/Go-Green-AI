package com.gogreen.ai.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nurseries", indexes = {
        @Index(name = "idx_nurseries_name", columnList = "name"),
        @Index(name = "idx_nurseries_user_id", columnList = "user_id")
})
public class Nursery extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String name;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String address;

    @Size(max = 100)
    @Column(length = 100)
    private String city;

    @Size(max = 20)
    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Email
    @Size(max = 150)
    @Column(name = "contact_email", length = 150)
    private String contactEmail;

    @Size(max = 20)
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Min(0)
    @Max(5)
    @Column
    private Double rating;

    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    @Column(name = "approval_status", nullable = false, length = 30)
    private com.gogreen.ai.entity.enums.NurseryApprovalStatus approvalStatus = com.gogreen.ai.entity.enums.NurseryApprovalStatus.PENDING;

    @Size(max = 500)
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Size(max = 500)
    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;
}
