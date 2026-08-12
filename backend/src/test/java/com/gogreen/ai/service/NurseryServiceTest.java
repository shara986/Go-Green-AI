package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.NurseryRequestDto;
import com.gogreen.ai.dto.response.NurseryResponseDto;
import com.gogreen.ai.entity.Nursery;
import com.gogreen.ai.entity.User;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.NurseryMapper;
import com.gogreen.ai.repository.NurseryRepository;
import com.gogreen.ai.repository.UserRepository;
import com.gogreen.ai.service.impl.NurseryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NurseryServiceTest {

    @Mock
    private NurseryRepository nurseryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NurseryMapper nurseryMapper;

    @InjectMocks
    private NurseryServiceImpl nurseryService;

    @Test
    void shouldGetNurseryById() {
        UUID id = UUID.randomUUID();
        Nursery nursery = new Nursery();
        nursery.setId(id);
        nursery.setName("Green Thumb Nursery");

        NurseryResponseDto responseDto = new NurseryResponseDto();
        responseDto.setId(id);
        responseDto.setName("Green Thumb Nursery");

        when(nurseryRepository.findById(id)).thenReturn(Optional.of(nursery));
        when(nurseryMapper.toResponseDto(nursery)).thenReturn(responseDto);

        NurseryResponseDto result = nurseryService.getNurseryById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Green Thumb Nursery", result.getName());
    }

    @Test
    void shouldRegisterNurserySuccessfully() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        NurseryRequestDto requestDto = new NurseryRequestDto();
        requestDto.setUserId(userId);
        requestDto.setName("Green Thumb Nursery");
        requestDto.setAddress("123 Green St");

        Nursery nursery = new Nursery();
        nursery.setName("Green Thumb Nursery");

        NurseryResponseDto responseDto = new NurseryResponseDto();
        responseDto.setName("Green Thumb Nursery");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(nurseryRepository.existsByUserId(userId)).thenReturn(false);
        when(nurseryMapper.toEntity(requestDto)).thenReturn(nursery);
        when(nurseryRepository.save(nursery)).thenReturn(nursery);
        when(nurseryMapper.toResponseDto(nursery)).thenReturn(responseDto);

        NurseryResponseDto result = nurseryService.registerNursery(requestDto);

        assertNotNull(result);
        assertEquals("Green Thumb Nursery", result.getName());
        verify(nurseryRepository).save(nursery);
    }

    @Test
    void shouldThrowConflictWhenRegisteringDuplicateNurseryForUser() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        NurseryRequestDto requestDto = new NurseryRequestDto();
        requestDto.setUserId(userId);
        requestDto.setName("Green Thumb Nursery");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(nurseryRepository.existsByUserId(userId)).thenReturn(true);

        APIException exception = assertThrows(APIException.class, () -> nurseryService.registerNursery(requestDto));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }
}
