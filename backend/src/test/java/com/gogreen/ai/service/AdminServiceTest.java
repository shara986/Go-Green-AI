package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.AdminNotificationRequestDto;
import com.gogreen.ai.dto.request.AnnouncementRequestDto;
import com.gogreen.ai.dto.response.AdminDashboardResponseDto;
import com.gogreen.ai.dto.response.AnnouncementResponseDto;
import com.gogreen.ai.entity.Announcement;
import com.gogreen.ai.entity.enums.UserRole;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.AdminMapper;
import com.gogreen.ai.mapper.AnnouncementMapper;
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
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
    private AnnouncementMapper announcementMapper;
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

    @Test
    void shouldSendAnnouncement() {
        AdminNotificationRequestDto requestDto = new AdminNotificationRequestDto();
        requestDto.setTitle("Plant Sale");
        requestDto.setMessage("Huge discounts this weekend!");

        adminService.sendAnnouncement(requestDto);

        verify(announcementRepository).save(any(Announcement.class));
    }

    @Test
    void shouldGetAllAnnouncements() {
        Announcement announcement = new Announcement();
        announcement.setTitle("Title");
        announcement.setMessage("Msg");

        AnnouncementResponseDto responseDto = new AnnouncementResponseDto();
        responseDto.setTitle("Title");
        responseDto.setMessage("Msg");

        when(announcementRepository.findAll()).thenReturn(List.of(announcement));
        when(announcementMapper.toResponseDtoList(List.of(announcement))).thenReturn(List.of(responseDto));

        List<AnnouncementResponseDto> result = adminService.getAnnouncements();

        assertEquals(1, result.size());
        assertEquals("Title", result.get(0).getTitle());
    }

    @Test
    void shouldGetAnnouncementById() {
        UUID id = UUID.randomUUID();
        Announcement announcement = new Announcement();
        announcement.setId(id);
        announcement.setTitle("Title");

        AnnouncementResponseDto responseDto = new AnnouncementResponseDto();
        responseDto.setId(id);
        responseDto.setTitle("Title");

        when(announcementRepository.findById(id)).thenReturn(Optional.of(announcement));
        when(announcementMapper.toResponseDto(announcement)).thenReturn(responseDto);

        AnnouncementResponseDto result = adminService.getAnnouncementById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void shouldThrowNotFoundWhenGettingNonExistentAnnouncementById() {
        UUID id = UUID.randomUUID();
        when(announcementRepository.findById(id)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> adminService.getAnnouncementById(id));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Announcement not found", exception.getMessage());
    }

    @Test
    void shouldCreateAnnouncement() {
        AnnouncementRequestDto requestDto = new AnnouncementRequestDto();
        requestDto.setTitle("New Announcement");
        requestDto.setMessage("New Message");
        requestDto.setActive(true);

        Announcement announcement = new Announcement();
        announcement.setTitle("New Announcement");
        announcement.setMessage("New Message");
        announcement.setActive(true);

        AnnouncementResponseDto responseDto = new AnnouncementResponseDto();
        responseDto.setTitle("New Announcement");
        responseDto.setMessage("New Message");
        responseDto.setActive(true);

        when(announcementMapper.toEntity(requestDto)).thenReturn(announcement);
        when(announcementRepository.save(announcement)).thenReturn(announcement);
        when(announcementMapper.toResponseDto(announcement)).thenReturn(responseDto);

        AnnouncementResponseDto result = adminService.createAnnouncement(requestDto);

        assertNotNull(result);
        assertEquals("New Announcement", result.getTitle());
        verify(announcementRepository).save(announcement);
    }

    @Test
    void shouldUpdateAnnouncement() {
        UUID id = UUID.randomUUID();
        AnnouncementRequestDto requestDto = new AnnouncementRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setMessage("Updated Message");
        requestDto.setActive(false);

        Announcement announcement = new Announcement();
        announcement.setId(id);
        announcement.setTitle("Old Title");
        announcement.setMessage("Old Message");
        announcement.setActive(true);

        AnnouncementResponseDto responseDto = new AnnouncementResponseDto();
        responseDto.setId(id);
        responseDto.setTitle("Updated Title");
        responseDto.setMessage("Updated Message");
        responseDto.setActive(false);

        when(announcementRepository.findById(id)).thenReturn(Optional.of(announcement));
        when(announcementRepository.save(announcement)).thenReturn(announcement);
        when(announcementMapper.toResponseDto(announcement)).thenReturn(responseDto);

        AnnouncementResponseDto result = adminService.updateAnnouncement(id, requestDto);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(announcementRepository).save(announcement);
    }

    @Test
    void shouldDeleteAnnouncement() {
        UUID id = UUID.randomUUID();
        Announcement announcement = new Announcement();
        announcement.setId(id);

        when(announcementRepository.findById(id)).thenReturn(Optional.of(announcement));

        adminService.deleteAnnouncement(id);

        verify(announcementRepository).delete(announcement);
    }
}
