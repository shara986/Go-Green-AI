package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.PlantRequestDto;
import com.gogreen.ai.dto.response.PageResponseDto;
import com.gogreen.ai.dto.response.PlantResponseDto;
import com.gogreen.ai.entity.Category;
import com.gogreen.ai.entity.Nursery;
import com.gogreen.ai.entity.Plant;
import com.gogreen.ai.entity.enums.PlantType;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.PlantMapper;
import com.gogreen.ai.repository.CategoryRepository;
import com.gogreen.ai.repository.NurseryRepository;
import com.gogreen.ai.repository.PlantRepository;
import com.gogreen.ai.service.impl.PlantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantServiceTest {

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private NurseryRepository nurseryRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PlantMapper plantMapper;

    @InjectMocks
    private PlantServiceImpl plantService;

    @Test
    void shouldGetAllPlantsPaginated() {
        Plant plant = new Plant();
        plant.setName("Snake Plant");

        PlantResponseDto responseDto = new PlantResponseDto();
        responseDto.setName("Snake Plant");

        Page<Plant> plantPage = new PageImpl<>(List.of(plant));

        when(plantRepository.searchPlants(eq("Snake"), eq(true), any(Pageable.class))).thenReturn(plantPage);
        when(plantMapper.toResponseDtoList(List.of(plant))).thenReturn(List.of(responseDto));

        PageResponseDto<PlantResponseDto> result = plantService.getAllPlants("Snake", true, 0, 10, "createdAt", "desc");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Snake Plant", result.getContent().get(0).getName());
    }

    @Test
    void shouldGetPlantById() {
        UUID id = UUID.randomUUID();
        Plant plant = new Plant();
        plant.setId(id);
        plant.setName("Monstera");

        PlantResponseDto responseDto = new PlantResponseDto();
        responseDto.setId(id);
        responseDto.setName("Monstera");

        when(plantRepository.findById(id)).thenReturn(Optional.of(plant));
        when(plantMapper.toResponseDto(plant)).thenReturn(responseDto);

        PlantResponseDto result = plantService.getPlantById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Monstera", result.getName());
    }

    @Test
    void shouldCreatePlantSuccessfully() {
        UUID nurseryId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Nursery nursery = new Nursery();
        nursery.setId(nurseryId);

        Category category = new Category();
        category.setId(categoryId);

        PlantRequestDto requestDto = new PlantRequestDto();
        requestDto.setNurseryId(nurseryId);
        requestDto.setCategoryId(categoryId);
        requestDto.setSku("PLANT-123");
        requestDto.setName("Aloe Vera");
        requestDto.setPrice(15.99);
        requestDto.setStock(20);
        requestDto.setPlantType(PlantType.INDOOR);

        Plant plant = new Plant();
        plant.setSku("PLANT-123");
        plant.setName("Aloe Vera");

        PlantResponseDto responseDto = new PlantResponseDto();
        responseDto.setSku("PLANT-123");
        responseDto.setName("Aloe Vera");

        when(nurseryRepository.findById(nurseryId)).thenReturn(Optional.of(nursery));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(plantRepository.existsByNurseryIdAndSku(nurseryId, "PLANT-123")).thenReturn(false);
        when(plantMapper.toEntity(requestDto)).thenReturn(plant);
        when(plantRepository.save(plant)).thenReturn(plant);
        when(plantMapper.toResponseDto(plant)).thenReturn(responseDto);

        PlantResponseDto result = plantService.createPlant(requestDto);

        assertNotNull(result);
        assertEquals("Aloe Vera", result.getName());
        verify(plantRepository).save(plant);
    }

    @Test
    void shouldThrowConflictWhenCreatingPlantWithDuplicateSku() {
        UUID nurseryId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Nursery nursery = new Nursery();
        nursery.setId(nurseryId);

        Category category = new Category();
        category.setId(categoryId);

        PlantRequestDto requestDto = new PlantRequestDto();
        requestDto.setNurseryId(nurseryId);
        requestDto.setCategoryId(categoryId);
        requestDto.setSku("PLANT-123");

        when(nurseryRepository.findById(nurseryId)).thenReturn(Optional.of(nursery));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(plantRepository.existsByNurseryIdAndSku(nurseryId, "PLANT-123")).thenReturn(true);

        APIException exception = assertThrows(APIException.class, () -> plantService.createPlant(requestDto));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    void shouldDeletePlant() {
        UUID id = UUID.randomUUID();
        Plant plant = new Plant();
        plant.setId(id);

        when(plantRepository.findById(id)).thenReturn(Optional.of(plant));

        plantService.deletePlant(id);

        verify(plantRepository).delete(plant);
    }
}
