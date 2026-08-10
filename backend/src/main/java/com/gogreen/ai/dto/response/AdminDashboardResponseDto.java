package com.gogreen.ai.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDashboardResponseDto {
    private long totalUsers;
    private long totalCustomers;
    private long totalNurseryOwners;
    private long totalGardeningExperts;
    private long totalDeliveryPartners;
    private long totalPlants;
    private long totalCategories;
    private long totalOrders;
    private double totalRevenue;
    private long pendingNurseryApprovals;
    private long pendingExpertApprovals;
    private long pendingDeliveryPartnerApprovals;
}
