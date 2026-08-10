package com.gogreen.ai.service;

import com.gogreen.ai.dto.response.AdminDashboardResponseDto;
import com.gogreen.ai.entity.enums.UserRole;
import com.gogreen.ai.mapper.AdminMapper;
import com.gogreen.ai.repository.AnnouncementRepository;
import com.gogreen.ai.repository.CategoryRepository;
import com.gogreen.ai.service.impl.AdminServiceImpl;
import com.gogreen.ai.repository.NurseryRepository;
import com.gogreen.ai.repository.OrderRepository;
import com.gogreen.ai.repository.PaymentRepository;
import com.gogreen.ai.repository.PlantRepository;
import com.gogreen.ai.repository.ReviewRepository;
import com.gogreen.ai.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private NurseryRepository nurseryRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private PlantRepository plantRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private AnnouncementRepository announcementRepository;
    @Mock
    private AdminMapper adminMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void shouldReturnDashboardCounts() {
        when(userRepository.count()).thenReturn(10L);
        when(userRepository.countByDeletedFalseAndRolesName(UserRole.ROLE_CUSTOMER)).thenReturn(4L);
        when(userRepository.countByDeletedFalseAndRolesName(UserRole.ROLE_NURSERY_OWNER)).thenReturn(2L);
        when(userRepository.countByDeletedFalseAndRolesName(UserRole.ROLE_GARDENING_EXPERT)).thenReturn(1L);
        when(userRepository.countByDeletedFalseAndRolesName(UserRole.ROLE_DELIVERY_PARTNER)).thenReturn(1L);
        when(categoryRepository.count()).thenReturn(5L);
        when(plantRepository.count()).thenReturn(8L);
        when(orderRepository.count()).thenReturn(6L);
        when(orderRepository.sumTotalAmount()).thenReturn(1200.0);
        when(nurseryRepository.countByApprovalStatus(com.gogreen.ai.entity.enums.NurseryApprovalStatus.PENDING)).thenReturn(1L);
        when(userRepository.countByDeletedFalseAndRolesNameAndApprovalStatus(UserRole.ROLE_GARDENING_EXPERT, com.gogreen.ai.entity.enums.UserApprovalStatus.PENDING_APPROVAL)).thenReturn(1L);
        when(userRepository.countByDeletedFalseAndRolesNameAndApprovalStatus(UserRole.ROLE_DELIVERY_PARTNER, com.gogreen.ai.entity.enums.UserApprovalStatus.PENDING_APPROVAL)).thenReturn(1L);

        AdminDashboardResponseDto expectedDashboard = new AdminDashboardResponseDto();
        expectedDashboard.setTotalUsers(10L);
        expectedDashboard.setTotalCustomers(4L);
        expectedDashboard.setTotalNurseryOwners(2L);
        expectedDashboard.setTotalGardeningExperts(1L);
        expectedDashboard.setTotalDeliveryPartners(1L);
        expectedDashboard.setTotalPlants(8L);
        expectedDashboard.setTotalCategories(5L);
        expectedDashboard.setTotalOrders(6L);
        expectedDashboard.setTotalRevenue(1200.0);
        expectedDashboard.setPendingNurseryApprovals(1L);
        expectedDashboard.setPendingExpertApprovals(1L);
        expectedDashboard.setPendingDeliveryPartnerApprovals(1L);
        when(adminMapper.toDashboardResponseDto(10L, 4L, 2L, 1L, 1L, 8L, 5L, 6L, 1200.0, 1L, 1L, 1L)).thenReturn(expectedDashboard);

        AdminDashboardResponseDto dashboard = adminService.getDashboard();

        assertEquals(10L, dashboard.getTotalUsers());
        assertEquals(4L, dashboard.getTotalCustomers());
        assertEquals(2L, dashboard.getTotalNurseryOwners());
        assertEquals(1L, dashboard.getTotalGardeningExperts());
        assertEquals(1L, dashboard.getTotalDeliveryPartners());
        assertEquals(8L, dashboard.getTotalPlants());
        assertEquals(5L, dashboard.getTotalCategories());
        assertEquals(6L, dashboard.getTotalOrders());
        assertEquals(1200.0, dashboard.getTotalRevenue());
        assertEquals(1L, dashboard.getPendingNurseryApprovals());
        assertEquals(1L, dashboard.getPendingExpertApprovals());
        assertEquals(1L, dashboard.getPendingDeliveryPartnerApprovals());
    }
}
