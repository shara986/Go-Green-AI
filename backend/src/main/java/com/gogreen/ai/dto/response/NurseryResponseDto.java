package com.gogreen.ai.dto.response;

import com.gogreen.ai.entity.enums.NurseryApprovalStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class NurseryResponseDto {

    private UUID id;
    private UUID userId;
    private String name;
    private String description;
    private String address;
    private String city;
    private String postalCode;
    private String contactEmail;
    private String contactPhone;
    private Double rating;
    private boolean verified;
    private NurseryApprovalStatus approvalStatus;
    private String logoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
