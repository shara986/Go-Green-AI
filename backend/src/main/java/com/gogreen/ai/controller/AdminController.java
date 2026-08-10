package com.gogreen.ai.controller;

import com.gogreen.ai.dto.ApiResponse;
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
import com.gogreen.ai.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin", description = "Administrative workflows for users, nurseries, categories, plants, orders, payments, feedback, announcements, and analytics")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Get admin dashboard", description = "Returns overall counts for users, plants, orders, categories, revenue, and pending approvals")
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponseDto>> getDashboard() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Dashboard loaded", adminService.getDashboard()));
    }

    @Operation(summary = "List users", description = "Retrieves users with optional filters for search, role, approval state, and enabled status, with pagination and sorting")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PageResponseDto<UserResponseDto>>> getUsers(AdminUserFilterRequestDto filter) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved", adminService.getUsers(filter)));
    }

    @Operation(summary = "Get user by ID", description = "Returns detailed information about a single user")
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "User retrieved", adminService.getUserById(userId)));
    }

    @Operation(summary = "Activate user", description = "Re-enables and approves a deactivated user account")
    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<ApiResponse<UserResponseDto>> activateUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "User activated", adminService.activateUser(userId)));
    }

    @Operation(summary = "Deactivate user", description = "Disables a user account")
    @PutMapping("/users/{userId}/deactivate")
    public ResponseEntity<ApiResponse<UserResponseDto>> deactivateUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "User deactivated", adminService.deactivateUser(userId)));
    }

    @Operation(summary = "Soft delete user", description = "Soft-deletes a user account")
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable UUID userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted", null));
    }

    @Operation(summary = "List all nurseries", description = "Returns all nurseries with optional keyword search and pagination")
    @GetMapping("/nurseries")
    public ResponseEntity<ApiResponse<PageResponseDto<NurseryResponseDto>>> getNurseries(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Nurseries retrieved", adminService.getNurseries(search, page, size)));
    }

    @Operation(summary = "List pending nurseries", description = "Returns nurseries awaiting admin approval")
    @GetMapping("/nurseries/pending")
    public ResponseEntity<ApiResponse<PageResponseDto<NurseryResponseDto>>> getPendingNurseries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Pending nurseries retrieved", adminService.getPendingNurseries(page, size)));
    }

    @Operation(summary = "Get nursery by ID", description = "Returns detailed information about a nursery")
    @GetMapping("/nurseries/{nurseryId}")
    public ResponseEntity<ApiResponse<NurseryResponseDto>> getNurseryById(@PathVariable UUID nurseryId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Nursery retrieved", adminService.getNurseryById(nurseryId)));
    }

    @Operation(summary = "Approve nursery", description = "Approves a pending nursery registration")
    @PutMapping("/nurseries/{nurseryId}/approve")
    public ResponseEntity<ApiResponse<NurseryResponseDto>> approveNursery(@PathVariable UUID nurseryId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Nursery approved", adminService.approveNursery(nurseryId)));
    }

    @Operation(summary = "Reject nursery", description = "Rejects a pending nursery registration, optionally with a reason")
    @PutMapping("/nurseries/{nurseryId}/reject")
    public ResponseEntity<ApiResponse<NurseryResponseDto>> rejectNursery(@PathVariable UUID nurseryId,
                                                                         @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Nursery rejected", adminService.rejectNursery(nurseryId, reason)));
    }

    @Operation(summary = "Suspend nursery", description = "Suspends an approved nursery")
    @PutMapping("/nurseries/{nurseryId}/suspend")
    public ResponseEntity<ApiResponse<NurseryResponseDto>> suspendNursery(@PathVariable UUID nurseryId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Nursery suspended", adminService.suspendNursery(nurseryId)));
    }

    @Operation(summary = "Activate nursery", description = "Reactivates a suspended or rejected nursery")
    @PutMapping("/nurseries/{nurseryId}/activate")
    public ResponseEntity<ApiResponse<NurseryResponseDto>> activateNursery(@PathVariable UUID nurseryId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Nursery activated", adminService.activateNursery(nurseryId)));
    }

    @Operation(summary = "List pending experts", description = "Returns gardening experts awaiting admin approval")
    @GetMapping("/experts/pending")
    public ResponseEntity<ApiResponse<PageResponseDto<UserResponseDto>>> getPendingExperts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Pending experts retrieved", adminService.getPendingExperts(page, size)));
    }

    @Operation(summary = "Get expert by ID", description = "Returns detailed information about a gardening expert")
    @GetMapping("/experts/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getExpertById(@PathVariable UUID userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Expert retrieved", adminService.getExpertById(userId)));
    }

    @Operation(summary = "Approve expert", description = "Approves a pending gardening expert")
    @PutMapping("/experts/{userId}/approve")
    public ResponseEntity<ApiResponse<UserResponseDto>> approveExpert(@PathVariable UUID userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Expert approved", adminService.approveExpert(userId)));
    }

    @Operation(summary = "Reject expert", description = "Rejects a pending gardening expert, optionally with a reason")
    @PutMapping("/experts/{userId}/reject")
    public ResponseEntity<ApiResponse<UserResponseDto>> rejectExpert(@PathVariable UUID userId,
                                                                     @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Expert rejected", adminService.rejectExpert(userId, reason)));
    }

    @Operation(summary = "Suspend expert", description = "Suspends a gardening expert")
    @PutMapping("/experts/{userId}/suspend")
    public ResponseEntity<ApiResponse<UserResponseDto>> suspendExpert(@PathVariable UUID userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Expert suspended", adminService.suspendExpert(userId)));
    }

    @Operation(summary = "Activate expert", description = "Reactivates a suspended or rejected gardening expert")
    @PutMapping("/experts/{userId}/activate")
    public ResponseEntity<ApiResponse<UserResponseDto>> activateExpert(@PathVariable UUID userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Expert activated", adminService.activateExpert(userId)));
    }

    @Operation(summary = "List delivery partners", description = "Returns all delivery partners")
    @GetMapping("/delivery-partners")
    public ResponseEntity<ApiResponse<PageResponseDto<UserResponseDto>>> getDeliveryPartners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Delivery partners retrieved", adminService.getDeliveryPartners(page, size)));
    }

    @Operation(summary = "Approve delivery partner", description = "Approves a delivery partner")
    @PutMapping("/delivery-partners/{userId}/approve")
    public ResponseEntity<ApiResponse<UserResponseDto>> approveDeliveryPartner(@PathVariable UUID userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Delivery partner approved", adminService.approveDeliveryPartner(userId)));
    }

    @Operation(summary = "Suspend delivery partner", description = "Suspends a delivery partner")
    @PutMapping("/delivery-partners/{userId}/suspend")
    public ResponseEntity<ApiResponse<UserResponseDto>> suspendDeliveryPartner(@PathVariable UUID userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Delivery partner suspended", adminService.suspendDeliveryPartner(userId)));
    }

    @Operation(summary = "Activate delivery partner", description = "Reactivates a suspended delivery partner")
    @PutMapping("/delivery-partners/{userId}/activate")
    public ResponseEntity<ApiResponse<UserResponseDto>> activateDeliveryPartner(@PathVariable UUID userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Delivery partner activated", adminService.activateDeliveryPartner(userId)));
    }

    @Operation(summary = "List categories", description = "Returns all plant categories with pagination")
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<PageResponseDto<AdminCategoryResponseDto>>> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Categories retrieved", adminService.getCategories(page, size)));
    }

    @Operation(summary = "Get category by ID", description = "Returns a single plant category")
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<AdminCategoryResponseDto>> getCategoryById(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Category retrieved", adminService.getCategoryById(categoryId)));
    }

    @Operation(summary = "Create a category", description = "Creates a new category for plant organization")
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<AdminCategoryResponseDto>> createCategory(@Valid @RequestBody AdminCategoryRequestDto requestDto) {
        return new ResponseEntity<>(new ApiResponse<>(true, "Category created", adminService.createCategory(requestDto)), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a category", description = "Updates an existing plant category")
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<AdminCategoryResponseDto>> updateCategory(@PathVariable UUID categoryId, @Valid @RequestBody AdminCategoryRequestDto requestDto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Category updated", adminService.updateCategory(categoryId, requestDto)));
    }

    @Operation(summary = "Delete a category", description = "Soft-deletes a plant category")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable UUID categoryId) {
        adminService.deleteCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category deleted", null));
    }

    @Operation(summary = "List plants", description = "Returns all plant listings with optional search and active filter")
    @GetMapping("/plants")
    public ResponseEntity<ApiResponse<PageResponseDto<PlantResponseDto>>> getPlants(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Plants retrieved", adminService.getPlants(search, active, page, size)));
    }

    @Operation(summary = "Create plant", description = "Creates a plant listing for an existing nursery and category")
    @PostMapping("/plants")
    public ResponseEntity<ApiResponse<PlantResponseDto>> createPlant(@Valid @RequestBody PlantRequestDto requestDto) {
        return new ResponseEntity<>(new ApiResponse<>(true, "Plant created", adminService.createPlant(requestDto)), HttpStatus.CREATED);
    }

    @Operation(summary = "Get plant by ID", description = "Returns detailed information about a plant listing")
    @GetMapping("/plants/{plantId}")
    public ResponseEntity<ApiResponse<PlantResponseDto>> getPlantById(@PathVariable UUID plantId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Plant retrieved", adminService.getPlantById(plantId)));
    }

    @Operation(summary = "Disable plant listing", description = "Disables a plant listing from the catalog")
    @PutMapping("/plants/{plantId}/disable")
    public ResponseEntity<ApiResponse<PlantResponseDto>> disablePlant(@PathVariable UUID plantId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Plant disabled", adminService.disablePlant(plantId)));
    }

    @Operation(summary = "Enable plant listing", description = "Re-enables a disabled plant listing")
    @PutMapping("/plants/{plantId}/enable")
    public ResponseEntity<ApiResponse<PlantResponseDto>> enablePlant(@PathVariable UUID plantId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Plant enabled", adminService.enablePlant(plantId)));
    }

    @Operation(summary = "Remove inappropriate plant", description = "Soft-deletes an inappropriate plant listing")
    @DeleteMapping("/plants/{plantId}")
    public ResponseEntity<ApiResponse<String>> removeInappropriatePlant(@PathVariable UUID plantId) {
        adminService.removeInappropriatePlant(plantId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Plant removed", null));
    }

    @Operation(summary = "Plant statistics", description = "Returns aggregate statistics for plants")
    @GetMapping("/plants/statistics")
    public ResponseEntity<ApiResponse<AdminPlantStatisticsResponseDto>> getPlantStatistics() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Plant statistics retrieved", adminService.getPlantStatistics()));
    }

    @Operation(summary = "List orders", description = "Returns all platform orders with optional search and status filter")
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<PageResponseDto<OrderResponseDto>>> getOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Orders retrieved", adminService.getOrders(search, status, page, size)));
    }

    @Operation(summary = "Get order by ID", description = "Returns detailed information about an order")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(@PathVariable UUID orderId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Order retrieved", adminService.getOrderById(orderId)));
    }

    @Operation(summary = "Cancel order", description = "Cancels an order when permitted")
    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelFraudulentOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Order cancelled", adminService.cancelFraudulentOrder(orderId)));
    }

    @Operation(summary = "List payments", description = "Returns payment history with optional search and status filter")
    @GetMapping("/payments")
    public ResponseEntity<ApiResponse<PageResponseDto<PaymentResponseDto>>> getPayments(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Payments retrieved", adminService.getPayments(search, status, page, size)));
    }

    @Operation(summary = "Get payment by ID", description = "Returns detailed information about a payment")
    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPaymentById(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Payment retrieved", adminService.getPaymentById(paymentId)));
    }

    @Operation(summary = "Total revenue", description = "Returns total platform revenue from completed orders")
    @GetMapping("/payments/revenue")
    public ResponseEntity<ApiResponse<Double>> getRevenue() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Revenue retrieved", adminService.getRevenue()));
    }

    @Operation(summary = "Monthly revenue report", description = "Returns the monthly revenue report")
    @GetMapping("/payments/monthly-revenue")
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>> getMonthlyRevenueReport() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Monthly revenue report retrieved", adminService.getMonthlyRevenueReport()));
    }

    @Operation(summary = "List feedback", description = "Returns customer feedback and reviews with optional search")
    @GetMapping("/feedback")
    public ResponseEntity<ApiResponse<PageResponseDto<ReviewResponseDto>>> getFeedbacks(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Feedback retrieved", adminService.getFeedbacks(search, page, size)));
    }

    @Operation(summary = "Delete abusive review", description = "Removes an abusive or inappropriate review")
    @DeleteMapping("/feedback/{reviewId}")
    public ResponseEntity<ApiResponse<String>> deleteAbusiveReview(@PathVariable UUID reviewId) {
        adminService.deleteAbusiveReview(reviewId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Review deleted", null));
    }

    @Operation(summary = "Send announcement", description = "Creates a platform-wide announcement")
    @PostMapping("/notifications/announcement")
    public ResponseEntity<ApiResponse<String>> sendAnnouncement(@Valid @RequestBody AdminNotificationRequestDto requestDto) {
        adminService.sendAnnouncement(requestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Announcement sent", null));
    }

    @Operation(summary = "Send promotional notification", description = "Sends a promotional notification")
    @PostMapping("/notifications/promotional")
    public ResponseEntity<ApiResponse<String>> sendPromotionalNotification(@Valid @RequestBody AdminNotificationRequestDto requestDto) {
        adminService.sendPromotionalNotification(requestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Promotional notification sent", null));
    }

    @Operation(summary = "Send maintenance notice", description = "Sends a maintenance notice")
    @PostMapping("/notifications/maintenance")
    public ResponseEntity<ApiResponse<String>> sendMaintenanceNotice(@Valid @RequestBody AdminNotificationRequestDto requestDto) {
        adminService.sendMaintenanceNotice(requestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Maintenance notice sent", null));
    }

    @Operation(summary = "List announcements", description = "Returns all platform announcements")
    @GetMapping("/announcements")
    public ResponseEntity<ApiResponse<List<AnnouncementResponseDto>>> getAnnouncements() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Announcements retrieved", adminService.getAnnouncements()));
    }

    @Operation(summary = "Get announcement by ID", description = "Returns a single announcement")
    @GetMapping("/announcements/{announcementId}")
    public ResponseEntity<ApiResponse<AnnouncementResponseDto>> getAnnouncementById(@PathVariable UUID announcementId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Announcement retrieved", adminService.getAnnouncementById(announcementId)));
    }

    @Operation(summary = "Create announcement", description = "Creates a platform-wide announcement")
    @PostMapping("/announcements")
    public ResponseEntity<ApiResponse<AnnouncementResponseDto>> createAnnouncement(@Valid @RequestBody AnnouncementRequestDto requestDto) {
        return new ResponseEntity<>(new ApiResponse<>(true, "Announcement created", adminService.createAnnouncement(requestDto)), HttpStatus.CREATED);
    }

    @Operation(summary = "Update announcement", description = "Updates an existing announcement")
    @PutMapping("/announcements/{announcementId}")
    public ResponseEntity<ApiResponse<AnnouncementResponseDto>> updateAnnouncement(@PathVariable UUID announcementId, @Valid @RequestBody AnnouncementRequestDto requestDto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Announcement updated", adminService.updateAnnouncement(announcementId, requestDto)));
    }

    @Operation(summary = "Delete announcement", description = "Deletes an announcement")
    @DeleteMapping("/announcements/{announcementId}")
    public ResponseEntity<ApiResponse<String>> deleteAnnouncement(@PathVariable UUID announcementId) {
        adminService.deleteAnnouncement(announcementId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Announcement deleted", null));
    }

    @Operation(summary = "Monthly sales analytics", description = "Returns monthly sales data for charts")
    @GetMapping("/analytics/monthly-sales")
    public ResponseEntity<ApiResponse<AdminChartDataResponseDto>> getMonthlySales() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Monthly sales analytics retrieved", adminService.getMonthlySales()));
    }

    @Operation(summary = "Top selling plants", description = "Returns the top selling plants")
    @GetMapping("/analytics/top-selling-plants")
    public ResponseEntity<ApiResponse<List<AdminTopSellingPlantResponseDto>>> getTopSellingPlants() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Top selling plants retrieved", adminService.getTopSellingPlants()));
    }

    @Operation(summary = "Nursery performance", description = "Returns nursery performance ranked by order count")
    @GetMapping("/analytics/nurseries")
    public ResponseEntity<ApiResponse<List<AdminNurseryActivityResponseDto>>> getNurseryPerformance() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Nursery performance retrieved", adminService.getNurseryPerformance()));
    }

    @Operation(summary = "Most active nursery", description = "Returns the most active nursery by order count")
    @GetMapping("/analytics/most-active-nursery")
    public ResponseEntity<ApiResponse<AdminNurseryActivityResponseDto>> getMostActiveNursery() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Most active nursery retrieved", adminService.getMostActiveNursery()));
    }

    @Operation(summary = "Revenue by month", description = "Returns revenue grouped by month for charts")
    @GetMapping("/analytics/revenue-by-month")
    public ResponseEntity<ApiResponse<AdminChartDataResponseDto>> getRevenueByMonth() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Revenue by month retrieved", adminService.getRevenueByMonth()));
    }

    @Operation(summary = "User growth", description = "Returns new user registrations grouped by month")
    @GetMapping("/analytics/user-growth")
    public ResponseEntity<ApiResponse<AdminChartDataResponseDto>> getUserGrowth() {
        return ResponseEntity.ok(new ApiResponse<>(true, "User growth retrieved", adminService.getUserGrowth()));
    }

    @Operation(summary = "Order growth", description = "Returns orders grouped by month")
    @GetMapping("/analytics/order-growth")
    public ResponseEntity<ApiResponse<AdminChartDataResponseDto>> getOrderGrowth() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Order growth retrieved", adminService.getOrderGrowth()));
    }

    @Operation(summary = "Plant category statistics", description = "Returns plant counts grouped by category")
    @GetMapping("/analytics/plant-category-statistics")
    public ResponseEntity<ApiResponse<AdminChartDataResponseDto>> getPlantCategoryStatistics() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Plant category statistics retrieved", adminService.getPlantCategoryStatistics()));
    }
}
