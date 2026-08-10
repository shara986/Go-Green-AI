package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.response.AdminCategoryResponseDto;
import com.gogreen.ai.dto.response.AdminDashboardResponseDto;
import com.gogreen.ai.dto.response.AdminPlantStatisticsResponseDto;
import com.gogreen.ai.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface AdminMapper {

    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "active", source = "active")
    AdminCategoryResponseDto toAdminCategoryResponseDto(Category category);

    List<AdminCategoryResponseDto> toAdminCategoryResponseDtoList(List<Category> categories);

    default AdminDashboardResponseDto toDashboardResponseDto(long totalUsers, long totalCustomers, long totalNurseryOwners,
                                                             long totalGardeningExperts, long totalDeliveryPartners,
                                                             long totalPlants, long totalCategories, long totalOrders,
                                                             double totalRevenue, long pendingNurseryApprovals,
                                                             long pendingExpertApprovals, long pendingDeliveryPartnerApprovals) {
        AdminDashboardResponseDto dto = new AdminDashboardResponseDto();
        dto.setTotalUsers(totalUsers);
        dto.setTotalCustomers(totalCustomers);
        dto.setTotalNurseryOwners(totalNurseryOwners);
        dto.setTotalGardeningExperts(totalGardeningExperts);
        dto.setTotalDeliveryPartners(totalDeliveryPartners);
        dto.setTotalPlants(totalPlants);
        dto.setTotalCategories(totalCategories);
        dto.setTotalOrders(totalOrders);
        dto.setTotalRevenue(totalRevenue);
        dto.setPendingNurseryApprovals(pendingNurseryApprovals);
        dto.setPendingExpertApprovals(pendingExpertApprovals);
        dto.setPendingDeliveryPartnerApprovals(pendingDeliveryPartnerApprovals);
        return dto;
    }

    default AdminPlantStatisticsResponseDto toPlantStatisticsResponseDto(long totalPlants, long activePlants, long inactivePlants, long plantsByCategory) {
        AdminPlantStatisticsResponseDto dto = new AdminPlantStatisticsResponseDto();
        dto.setTotalPlants(totalPlants);
        dto.setActivePlants(activePlants);
        dto.setInactivePlants(inactivePlants);
        dto.setPlantsByCategory(plantsByCategory);
        return dto;
    }
}
