package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.PlantRequestDto;
import com.gogreen.ai.dto.response.PageResponseDto;
import com.gogreen.ai.dto.response.PlantResponseDto;

import java.util.List;
import java.util.UUID;

public interface PlantService {

    PageResponseDto<PlantResponseDto> getAllPlants(String search, Boolean active, int pageNo, int pageSize, String sortBy, String sortDir);

    PlantResponseDto getPlantById(UUID id);

    List<PlantResponseDto> getPlantsByNursery(UUID nurseryId);

    List<PlantResponseDto> getPlantsByCategory(UUID categoryId);

    PlantResponseDto createPlant(PlantRequestDto plantRequestDto);

    PlantResponseDto updatePlant(UUID id, PlantRequestDto plantRequestDto);

    void deletePlant(UUID id);
}
