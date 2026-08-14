package com.gogreen.ai.service.impl;

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
import com.gogreen.ai.entity.Announcement;
import com.gogreen.ai.entity.Category;
import com.gogreen.ai.entity.Nursery;
import com.gogreen.ai.entity.Order;
import com.gogreen.ai.entity.Payment;
import com.gogreen.ai.entity.Plant;
import com.gogreen.ai.entity.Review;
import com.gogreen.ai.entity.User;
import com.gogreen.ai.entity.enums.NurseryApprovalStatus;
import com.gogreen.ai.entity.enums.NotificationType;
import com.gogreen.ai.entity.enums.OrderStatus;
import com.gogreen.ai.entity.enums.PaymentStatus;
import com.gogreen.ai.entity.enums.UserApprovalStatus;
import com.gogreen.ai.entity.enums.UserRole;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.AdminMapper;
import com.gogreen.ai.mapper.AnnouncementMapper;
import com.gogreen.ai.mapper.NurseryMapper;
import com.gogreen.ai.mapper.OrderMapper;
import com.gogreen.ai.mapper.PaymentMapper;
import com.gogreen.ai.mapper.PlantMapper;
import com.gogreen.ai.mapper.ReviewMapper;
import com.gogreen.ai.mapper.UserMapper;
import com.gogreen.ai.repository.AnnouncementRepository;
import com.gogreen.ai.repository.CategoryRepository;
import com.gogreen.ai.repository.NurseryRepository;
import com.gogreen.ai.repository.OrderRepository;
import com.gogreen.ai.repository.PaymentRepository;
import com.gogreen.ai.repository.PlantRepository;
import com.gogreen.ai.repository.ReviewRepository;
import com.gogreen.ai.repository.UserRepository;
import com.gogreen.ai.service.AdminService;
import com.gogreen.ai.util.CategorySlugGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final NurseryRepository nurseryRepository;
    private final CategoryRepository categoryRepository;
    private final PlantRepository plantRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;
    private final AnnouncementRepository announcementRepository;
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;
    private final PlantMapper plantMapper;
    private final NurseryMapper nurseryMapper;
    private final OrderMapper orderMapper;
    private final PaymentMapper paymentMapper;
    private final ReviewMapper reviewMapper;
    private final AnnouncementMapper announcementMapper;

    public AdminServiceImpl(UserRepository userRepository, NurseryRepository nurseryRepository,
                            CategoryRepository categoryRepository, PlantRepository plantRepository,
                            OrderRepository orderRepository, PaymentRepository paymentRepository,
                            ReviewRepository reviewRepository, AnnouncementRepository announcementRepository,
                            AdminMapper adminMapper, UserMapper userMapper,
                            PlantMapper plantMapper, NurseryMapper nurseryMapper,
                            OrderMapper orderMapper, PaymentMapper paymentMapper, ReviewMapper reviewMapper,
                            AnnouncementMapper announcementMapper) {
        this.userRepository = userRepository;
        this.nurseryRepository = nurseryRepository;
        this.categoryRepository = categoryRepository;
        this.plantRepository = plantRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.reviewRepository = reviewRepository;
        this.announcementRepository = announcementRepository;
        this.adminMapper = adminMapper;
        this.userMapper = userMapper;
        this.plantMapper = plantMapper;
        this.nurseryMapper = nurseryMapper;
        this.orderMapper = orderMapper;
        this.paymentMapper = paymentMapper;
        this.reviewMapper = reviewMapper;
        this.announcementMapper = announcementMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardResponseDto getDashboard() {
        long totalUsers = userRepository.count();
        long totalCustomers = userRepository.countByDeletedFalseAndRolesName(UserRole.ROLE_CUSTOMER);
        long totalNurseryOwners = userRepository.countByDeletedFalseAndRolesName(UserRole.ROLE_NURSERY_OWNER);
        long totalGardeningExperts = userRepository.countByDeletedFalseAndRolesName(UserRole.ROLE_GARDENING_EXPERT);
        long totalDeliveryPartners = userRepository.countByDeletedFalseAndRolesName(UserRole.ROLE_DELIVERY_PARTNER);
        long totalPlants = plantRepository.count();
        long totalCategories = categoryRepository.count();
        long totalOrders = orderRepository.count();
        double totalRevenue = orderRepository.sumTotalAmount() == null ? 0 : orderRepository.sumTotalAmount();
        long pendingNurseryApprovals = nurseryRepository.countByApprovalStatus(NurseryApprovalStatus.PENDING);
        long pendingExpertApprovals = userRepository.countByDeletedFalseAndRolesNameAndApprovalStatus(UserRole.ROLE_GARDENING_EXPERT, UserApprovalStatus.PENDING_APPROVAL);
        long pendingDeliveryPartnerApprovals = userRepository.countByDeletedFalseAndRolesNameAndApprovalStatus(UserRole.ROLE_DELIVERY_PARTNER, UserApprovalStatus.PENDING_APPROVAL);
        return adminMapper.toDashboardResponseDto(totalUsers, totalCustomers, totalNurseryOwners, totalGardeningExperts, totalDeliveryPartners, totalPlants, totalCategories, totalOrders, totalRevenue, pendingNurseryApprovals, pendingExpertApprovals, pendingDeliveryPartnerApprovals);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<UserResponseDto> getUsers(AdminUserFilterRequestDto filter) {
        Sort sort = Sort.by(filter.getSortDir().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, filter.getSortBy());
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);
        Page<User> page = userRepository.searchUsers(filter.getSearch(), filter.getRole(), filter.getApprovalStatus(), filter.getEnabled(), pageable);
        return toPageResponse(page.map(userMapper::toResponseDto));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(UUID userId) {
        User user = findUser(userId);
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto activateUser(UUID userId) {
        User user = findUser(userId);
        user.setEnabled(true);
        user.setApprovalStatus(UserApprovalStatus.APPROVED);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto deactivateUser(UUID userId) {
        User user = findUser(userId);
        user.setEnabled(false);
        user.setApprovalStatus(UserApprovalStatus.INACTIVE);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        User user = findUser(userId);
        user.setDeleted(true);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<NurseryResponseDto> getNurseries(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Nursery> pageResult = nurseryRepository.searchNurseries(search, pageable);
        return toPageResponse(pageResult.map(nurseryMapper::toResponseDto));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<NurseryResponseDto> getPendingNurseries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Nursery> pageResult = nurseryRepository.findByApprovalStatus(NurseryApprovalStatus.PENDING, pageable);
        return toPageResponse(pageResult.map(nurseryMapper::toResponseDto));
    }

    @Override
    @Transactional(readOnly = true)
    public NurseryResponseDto getNurseryById(UUID nurseryId) {
        Nursery nursery = findNursery(nurseryId);
        return nurseryMapper.toResponseDto(nursery);
    }

    @Override
    @Transactional
    public NurseryResponseDto approveNursery(UUID nurseryId) {
        Nursery nursery = findNursery(nurseryId);
        if (nursery.getApprovalStatus() != NurseryApprovalStatus.PENDING) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Only pending nurseries can be approved");
        }
        nursery.setVerified(true);
        nursery.setApprovalStatus(NurseryApprovalStatus.APPROVED);
        nursery.setRejectionReason(null);
        return nurseryMapper.toResponseDto(nurseryRepository.save(nursery));
    }

    @Override
    @Transactional
    public NurseryResponseDto rejectNursery(UUID nurseryId, String reason) {
        Nursery nursery = findNursery(nurseryId);
        if (nursery.getApprovalStatus() != NurseryApprovalStatus.PENDING) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Only pending nurseries can be rejected");
        }
        nursery.setApprovalStatus(NurseryApprovalStatus.REJECTED);
        nursery.setVerified(false);
        nursery.setRejectionReason(reason);
        return nurseryMapper.toResponseDto(nurseryRepository.save(nursery));
    }

    @Override
    @Transactional
    public NurseryResponseDto suspendNursery(UUID nurseryId) {
        Nursery nursery = findNursery(nurseryId);
        nursery.setApprovalStatus(NurseryApprovalStatus.SUSPENDED);
        nursery.setVerified(false);
        return nurseryMapper.toResponseDto(nurseryRepository.save(nursery));
    }

    @Override
    @Transactional
    public NurseryResponseDto activateNursery(UUID nurseryId) {
        Nursery nursery = findNursery(nurseryId);
        nursery.setApprovalStatus(NurseryApprovalStatus.APPROVED);
        nursery.setVerified(true);
        return nurseryMapper.toResponseDto(nurseryRepository.save(nursery));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<UserResponseDto> getPendingExperts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> pageResult = userRepository.findByRolesNameAndApprovalStatus(UserRole.ROLE_GARDENING_EXPERT, UserApprovalStatus.PENDING_APPROVAL, pageable);
        return toPageResponse(pageResult.map(userMapper::toResponseDto));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getExpertById(UUID userId) {
        User user = findUserWithRole(userId, UserRole.ROLE_GARDENING_EXPERT);
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto approveExpert(UUID userId) {
        User user = findUserWithRole(userId, UserRole.ROLE_GARDENING_EXPERT);
        user.setApprovalStatus(UserApprovalStatus.APPROVED);
        user.setEnabled(true);
        user.setRejectionReason(null);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto rejectExpert(UUID userId, String reason) {
        User user = findUserWithRole(userId, UserRole.ROLE_GARDENING_EXPERT);
        user.setApprovalStatus(UserApprovalStatus.REJECTED);
        user.setEnabled(false);
        user.setRejectionReason(reason);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto suspendExpert(UUID userId) {
        User user = findUserWithRole(userId, UserRole.ROLE_GARDENING_EXPERT);
        user.setApprovalStatus(UserApprovalStatus.SUSPENDED);
        user.setEnabled(false);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto activateExpert(UUID userId) {
        User user = findUserWithRole(userId, UserRole.ROLE_GARDENING_EXPERT);
        user.setApprovalStatus(UserApprovalStatus.APPROVED);
        user.setEnabled(true);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<UserResponseDto> getDeliveryPartners(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> pageResult = userRepository.findByRolesName(UserRole.ROLE_DELIVERY_PARTNER, pageable);
        return toPageResponse(pageResult.map(userMapper::toResponseDto));
    }

    @Override
    @Transactional
    public UserResponseDto approveDeliveryPartner(UUID userId) {
        User user = findUserWithRole(userId, UserRole.ROLE_DELIVERY_PARTNER);
        user.setApprovalStatus(UserApprovalStatus.APPROVED);
        user.setEnabled(true);
        user.setRejectionReason(null);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto suspendDeliveryPartner(UUID userId) {
        User user = findUserWithRole(userId, UserRole.ROLE_DELIVERY_PARTNER);
        user.setApprovalStatus(UserApprovalStatus.SUSPENDED);
        user.setEnabled(false);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto activateDeliveryPartner(UUID userId) {
        User user = findUserWithRole(userId, UserRole.ROLE_DELIVERY_PARTNER);
        user.setApprovalStatus(UserApprovalStatus.APPROVED);
        user.setEnabled(true);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<AdminCategoryResponseDto> getCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> pageResult = categoryRepository.findAll(pageable);
        return toPageResponse(pageResult.map(adminMapper::toAdminCategoryResponseDto));
    }

    @Override
    @Transactional(readOnly = true)
    public AdminCategoryResponseDto getCategoryById(UUID categoryId) {
        Category category = findCategory(categoryId);
        return adminMapper.toAdminCategoryResponseDto(category);
    }

    @Override
    @Transactional
    public AdminCategoryResponseDto createCategory(AdminCategoryRequestDto requestDto) {
        if (categoryRepository.existsByName(requestDto.getName())) {
            throw new APIException(HttpStatus.CONFLICT, "Category already exists");
        }
        Category category = new Category();
        category.setName(requestDto.getName());
        category.setSlug(CategorySlugGenerator.generateUniqueSlug(requestDto.getName(), categoryRepository::existsBySlug));
        category.setDescription(requestDto.getDescription());
        category.setIcon(requestDto.getIcon());
        category.setActive(requestDto.getActive() == null ? true : requestDto.getActive());
        return adminMapper.toAdminCategoryResponseDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public AdminCategoryResponseDto updateCategory(UUID categoryId, AdminCategoryRequestDto requestDto) {
        Category category = findCategory(categoryId);
        if (requestDto.getName() != null && !requestDto.getName().equals(category.getName()) && categoryRepository.existsByName(requestDto.getName())) {
            throw new APIException(HttpStatus.CONFLICT, "Category already exists");
        }
        if (requestDto.getName() != null) {
            category.setName(requestDto.getName());
            category.setSlug(CategorySlugGenerator.generateUniqueSlug(requestDto.getName(), categoryRepository::existsBySlug));
        }
        if (requestDto.getDescription() != null) category.setDescription(requestDto.getDescription());
        if (requestDto.getIcon() != null) category.setIcon(requestDto.getIcon());
        if (requestDto.getActive() != null) category.setActive(requestDto.getActive());
        return adminMapper.toAdminCategoryResponseDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(UUID categoryId) {
        Category category = findCategory(categoryId);
        category.setActive(false);
        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<PlantResponseDto> getPlants(String search, Boolean active, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Plant> pageResult = plantRepository.searchPlants(search, active, pageable);
        return toPageResponse(pageResult.map(plantMapper::toResponseDto));
    }

    @Override
    @Transactional
    public PlantResponseDto createPlant(PlantRequestDto requestDto) {
        Nursery nursery = findNursery(requestDto.getNurseryId());
        Category category = findCategory(requestDto.getCategoryId());

        if (plantRepository.existsByNurseryIdAndSku(nursery.getId(), requestDto.getSku())) {
            throw new APIException(HttpStatus.CONFLICT, "A plant with this SKU already exists for the nursery");
        }

        Plant plant = plantMapper.toEntity(requestDto);
        plant.setNursery(nursery);
        plant.setCategory(category);
        return plantMapper.toResponseDto(plantRepository.save(plant));
    }

    @Override
    @Transactional(readOnly = true)
    public PlantResponseDto getPlantById(UUID plantId) {
        Plant plant = plantRepository.findById(plantId).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Plant not found"));
        return plantMapper.toResponseDto(plant);
    }

    @Override
    @Transactional
    public PlantResponseDto disablePlant(UUID plantId) {
        Plant plant = plantRepository.findById(plantId).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Plant not found"));
        plant.setActive(false);
        return plantMapper.toResponseDto(plantRepository.save(plant));
    }

    @Override
    @Transactional
    public PlantResponseDto enablePlant(UUID plantId) {
        Plant plant = plantRepository.findById(plantId).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Plant not found"));
        plant.setActive(true);
        return plantMapper.toResponseDto(plantRepository.save(plant));
    }

    @Override
    @Transactional
    public void removeInappropriatePlant(UUID plantId) {
        Plant plant = plantRepository.findById(plantId).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Plant not found"));
        plant.setActive(false);
        plantRepository.save(plant);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminPlantStatisticsResponseDto getPlantStatistics() {
        long totalPlants = plantRepository.count();
        long activePlants = plantRepository.countByActiveTrue();
        long inactivePlants = totalPlants - activePlants;
        long plantsByCategory = categoryRepository.count();
        return adminMapper.toPlantStatisticsResponseDto(totalPlants, activePlants, inactivePlants, plantsByCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<OrderResponseDto> getOrders(String search, OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> pageResult = orderRepository.searchOrders(search, status, pageable);
        return toPageResponse(pageResult.map(orderMapper::toResponseDto));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Order not found"));
        return orderMapper.toResponseDto(order);
    }

    @Override
    @Transactional
    public OrderResponseDto cancelFraudulentOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Order not found"));
        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Order cannot be cancelled in its current status");
        }
        order.setStatus(OrderStatus.CANCELLED);
        return orderMapper.toResponseDto(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<PaymentResponseDto> getPayments(String search, PaymentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> pageResult = paymentRepository.searchPayments(search, status, pageable);
        return toPageResponse(pageResult.map(paymentMapper::toResponseDto));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Payment not found"));
        return paymentMapper.toResponseDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public double getRevenue() {
        Double revenue = orderRepository.sumTotalAmount();
        return revenue == null ? 0 : revenue;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getMonthlyRevenueReport() {
        return paymentMapper.toResponseDtoList(paymentRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<ReviewResponseDto> getFeedbacks(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> pageResult = reviewRepository.searchReviews(search, pageable);
        return toPageResponse(pageResult.map(reviewMapper::toResponseDto));
    }

    @Override
    @Transactional
    public void deleteAbusiveReview(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Review not found"));
        reviewRepository.delete(review);
    }

    @Override
    @Transactional
    public void sendAnnouncement(AdminNotificationRequestDto requestDto) {
        Announcement announcement = new Announcement();
        announcement.setTitle(requestDto.getTitle());
        announcement.setMessage(requestDto.getMessage());
        announcement.setActive(true);
        announcementRepository.save(announcement);
    }

    @Override
    @Transactional
    public void sendPromotionalNotification(AdminNotificationRequestDto requestDto) {
        // promotional notifications are structured for future integration
    }

    @Override
    @Transactional
    public void sendMaintenanceNotice(AdminNotificationRequestDto requestDto) {
        // maintenance notices are structured for future integration
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponseDto> getAnnouncements() {
        return announcementMapper.toResponseDtoList(announcementRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponseDto getAnnouncementById(UUID announcementId) {
        Announcement announcement = findAnnouncement(announcementId);
        return announcementMapper.toResponseDto(announcement);
    }

    @Override
    @Transactional
    public AnnouncementResponseDto createAnnouncement(AnnouncementRequestDto requestDto) {
        Announcement announcement = announcementMapper.toEntity(requestDto);
        if (requestDto.getActive() != null) {
            announcement.setActive(requestDto.getActive());
        }
        return announcementMapper.toResponseDto(announcementRepository.save(announcement));
    }

    @Override
    @Transactional
    public AnnouncementResponseDto updateAnnouncement(UUID announcementId, AnnouncementRequestDto requestDto) {
        Announcement announcement = findAnnouncement(announcementId);
        if (requestDto.getTitle() != null) announcement.setTitle(requestDto.getTitle());
        if (requestDto.getMessage() != null) announcement.setMessage(requestDto.getMessage());
        if (requestDto.getActive() != null) announcement.setActive(requestDto.getActive());
        return announcementMapper.toResponseDto(announcementRepository.save(announcement));
    }

    @Override
    @Transactional
    public void deleteAnnouncement(UUID announcementId) {
        Announcement announcement = findAnnouncement(announcementId);
        announcementRepository.delete(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminChartDataResponseDto getMonthlySales() {
        return buildMonthSeries(orderRepository.sumRevenueByMonth());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminTopSellingPlantResponseDto> getTopSellingPlants() {
        List<Object[]> rows = orderRepository.findTopSellingPlants();
        List<AdminTopSellingPlantResponseDto> result = new ArrayList<>();
        for (Object[] row : rows) {
            AdminTopSellingPlantResponseDto dto = new AdminTopSellingPlantResponseDto();
            dto.setPlantName((String) row[0]);
            dto.setSoldCount(((Number) row[1]).longValue());
            result.add(dto);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminNurseryActivityResponseDto getMostActiveNursery() {
        List<Object[]> rows = nurseryRepository.findNurseryPerformance();
        if (rows.isEmpty()) {
            return new AdminNurseryActivityResponseDto();
        }
        Object[] row = rows.get(0);
        AdminNurseryActivityResponseDto dto = new AdminNurseryActivityResponseDto();
        dto.setNurseryName((String) row[0]);
        dto.setOrderCount(((Number) row[1]).longValue());
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminChartDataResponseDto getRevenueByMonth() {
        return buildMonthSeries(orderRepository.sumRevenueByMonth());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminChartDataResponseDto getUserGrowth() {
        return buildMonthSeries(userRepository.countUsersByMonth());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminChartDataResponseDto getOrderGrowth() {
        return buildMonthSeries(orderRepository.countOrdersByMonth());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminChartDataResponseDto getPlantCategoryStatistics() {
        return buildCategorySeries(plantRepository.countPlantsByCategory());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminNurseryActivityResponseDto> getNurseryPerformance() {
        List<Object[]> rows = nurseryRepository.findNurseryPerformance();
        List<AdminNurseryActivityResponseDto> result = new ArrayList<>();
        for (Object[] row : rows) {
            AdminNurseryActivityResponseDto dto = new AdminNurseryActivityResponseDto();
            dto.setNurseryName((String) row[0]);
            dto.setOrderCount(((Number) row[1]).longValue());
            result.add(dto);
        }
        return result;
    }

    private AdminChartDataResponseDto buildMonthSeries(List<Object[]> rows) {
        AdminChartDataResponseDto dto = new AdminChartDataResponseDto();
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        for (Object[] row : rows) {
            labels.add((String) row[0]);
            values.add(((Number) row[1]).doubleValue());
        }
        dto.setLabels(labels);
        dto.setValues(values);
        return dto;
    }

    private AdminChartDataResponseDto buildCategorySeries(List<Object[]> rows) {
        AdminChartDataResponseDto dto = new AdminChartDataResponseDto();
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        for (Object[] row : rows) {
            labels.add((String) row[0]);
            values.add(((Number) row[1]).doubleValue());
        }
        dto.setLabels(labels);
        dto.setValues(values);
        return dto;
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private User findUserWithRole(UUID userId, UserRole role) {
        User user = findUser(userId);
        boolean hasRole = user.getRoles() != null && user.getRoles().stream().anyMatch(r -> r.getName() == role);
        if (!hasRole) {
            throw new APIException(HttpStatus.BAD_REQUEST, "User does not have the required role");
        }
        return user;
    }

    private Nursery findNursery(UUID nurseryId) {
        return nurseryRepository.findById(nurseryId).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Nursery not found"));
    }

    private Category findCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    private Announcement findAnnouncement(UUID announcementId) {
        return announcementRepository.findById(announcementId).orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Announcement not found"));
    }

    private <T> PageResponseDto<T> toPageResponse(Page<T> page) {
        PageResponseDto<T> response = new PageResponseDto<>();
        response.setContent(page.getContent());
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        return response;
    }
}
