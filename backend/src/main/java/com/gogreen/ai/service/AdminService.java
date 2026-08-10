package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.AdminCategoryRequestDto;
import com.gogreen.ai.dto.request.AdminNotificationRequestDto;
import com.gogreen.ai.dto.request.AdminUserFilterRequestDto;
import com.gogreen.ai.dto.request.AnnouncementRequestDto;
import com.gogreen.ai.dto.request.PlantRequestDto;
import com.gogreen.ai.dto.response.AdminCategoryResponseDto;
import com.gogreen.ai.dto.response.AdminChartDataResponseDto;
import com.gogreen.ai.dto.response.AdminDashboardResponseDto;
import com.gogreen.ai.dto.response.AdminNurseryActivityResponseDto;
import com.gogreen.ai.dto.response.AdminPlantStatisticsResponseDto;
import com.gogreen.ai.dto.response.AdminTopSellingPlantResponseDto;
import com.gogreen.ai.dto.response.AnnouncementResponseDto;
import com.gogreen.ai.dto.response.NurseryResponseDto;
import com.gogreen.ai.dto.response.OrderResponseDto;
import com.gogreen.ai.dto.response.PageResponseDto;
import com.gogreen.ai.dto.response.PaymentResponseDto;
import com.gogreen.ai.dto.response.PlantResponseDto;
import com.gogreen.ai.dto.response.ReviewResponseDto;
import com.gogreen.ai.dto.response.UserResponseDto;
import com.gogreen.ai.entity.enums.OrderStatus;
import com.gogreen.ai.entity.enums.PaymentStatus;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    AdminDashboardResponseDto getDashboard();

    PageResponseDto<UserResponseDto> getUsers(AdminUserFilterRequestDto filter);

    UserResponseDto getUserById(UUID userId);

    UserResponseDto activateUser(UUID userId);

    UserResponseDto deactivateUser(UUID userId);

    void deleteUser(UUID userId);

    PageResponseDto<NurseryResponseDto> getNurseries(String search, int page, int size);

    PageResponseDto<NurseryResponseDto> getPendingNurseries(int page, int size);

    NurseryResponseDto getNurseryById(UUID nurseryId);

    NurseryResponseDto approveNursery(UUID nurseryId);

    NurseryResponseDto rejectNursery(UUID nurseryId, String reason);

    NurseryResponseDto suspendNursery(UUID nurseryId);

    NurseryResponseDto activateNursery(UUID nurseryId);

    PageResponseDto<UserResponseDto> getPendingExperts(int page, int size);

    UserResponseDto getExpertById(UUID userId);

    UserResponseDto approveExpert(UUID userId);

    UserResponseDto rejectExpert(UUID userId, String reason);

    UserResponseDto suspendExpert(UUID userId);

    UserResponseDto activateExpert(UUID userId);

    PageResponseDto<UserResponseDto> getDeliveryPartners(int page, int size);

    UserResponseDto approveDeliveryPartner(UUID userId);

    UserResponseDto suspendDeliveryPartner(UUID userId);

    UserResponseDto activateDeliveryPartner(UUID userId);

    PageResponseDto<AdminCategoryResponseDto> getCategories(int page, int size);

    AdminCategoryResponseDto getCategoryById(UUID categoryId);

    AdminCategoryResponseDto createCategory(AdminCategoryRequestDto requestDto);

    AdminCategoryResponseDto updateCategory(UUID categoryId, AdminCategoryRequestDto requestDto);

    void deleteCategory(UUID categoryId);

    PageResponseDto<PlantResponseDto> getPlants(String search, Boolean active, int page, int size);

    PlantResponseDto createPlant(PlantRequestDto requestDto);

    PlantResponseDto getPlantById(UUID plantId);

    PlantResponseDto disablePlant(UUID plantId);

    PlantResponseDto enablePlant(UUID plantId);

    void removeInappropriatePlant(UUID plantId);

    AdminPlantStatisticsResponseDto getPlantStatistics();

    PageResponseDto<OrderResponseDto> getOrders(String search, OrderStatus status, int page, int size);

    OrderResponseDto getOrderById(UUID orderId);

    OrderResponseDto cancelFraudulentOrder(UUID orderId);

    PageResponseDto<PaymentResponseDto> getPayments(String search, PaymentStatus status, int page, int size);

    PaymentResponseDto getPaymentById(UUID paymentId);

    double getRevenue();

    List<PaymentResponseDto> getMonthlyRevenueReport();

    PageResponseDto<ReviewResponseDto> getFeedbacks(String search, int page, int size);

    void deleteAbusiveReview(UUID reviewId);

    void sendAnnouncement(AdminNotificationRequestDto requestDto);

    void sendPromotionalNotification(AdminNotificationRequestDto requestDto);

    void sendMaintenanceNotice(AdminNotificationRequestDto requestDto);

    List<AnnouncementResponseDto> getAnnouncements();

    AnnouncementResponseDto getAnnouncementById(UUID announcementId);

    AnnouncementResponseDto createAnnouncement(AnnouncementRequestDto requestDto);

    AnnouncementResponseDto updateAnnouncement(UUID announcementId, AnnouncementRequestDto requestDto);

    void deleteAnnouncement(UUID announcementId);

    AdminChartDataResponseDto getMonthlySales();

    List<AdminTopSellingPlantResponseDto> getTopSellingPlants();

    AdminNurseryActivityResponseDto getMostActiveNursery();

    AdminChartDataResponseDto getRevenueByMonth();

    AdminChartDataResponseDto getUserGrowth();

    AdminChartDataResponseDto getOrderGrowth();

    AdminChartDataResponseDto getPlantCategoryStatistics();

    List<AdminNurseryActivityResponseDto> getNurseryPerformance();
}
