package com.gogreen.ai.service.impl;

import com.gogreen.ai.dto.request.PlantRequestDto;
import com.gogreen.ai.dto.response.PageResponseDto;
import com.gogreen.ai.dto.response.PlantResponseDto;
import com.gogreen.ai.entity.Category;
import com.gogreen.ai.entity.Nursery;
import com.gogreen.ai.entity.Plant;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.PlantMapper;
import com.gogreen.ai.repository.CategoryRepository;
import com.gogreen.ai.repository.NurseryRepository;
import com.gogreen.ai.repository.PlantRepository;
import com.gogreen.ai.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PlantServiceImpl implements PlantService {

    private final PlantRepository plantRepository;
    private final NurseryRepository nurseryRepository;
    private final CategoryRepository categoryRepository;
    private final PlantMapper plantMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<PlantResponseDto> getAllPlants(String search, Boolean active, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Plant> plants = plantRepository.searchPlants(search, active, pageable);

        PageResponseDto<PlantResponseDto> pageResponse = new PageResponseDto<>();
        pageResponse.setContent(plantMapper.toResponseDtoList(plants.getContent()));
        pageResponse.setPage(plants.getNumber());
        pageResponse.setSize(plants.getSize());
        pageResponse.setTotalElements(plants.getTotalElements());
        pageResponse.setTotalPages(plants.getTotalPages());

        return pageResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public PlantResponseDto getPlantById(UUID id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Plant not found with id: " + id));
        return plantMapper.toResponseDto(plant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlantResponseDto> getPlantsByNursery(UUID nurseryId) {
        if (!nurseryRepository.existsById(nurseryId)) {
            throw new APIException(HttpStatus.NOT_FOUND, "Nursery not found with id: " + nurseryId);
        }
        List<Plant> plants = plantRepository.findByNurseryId(nurseryId);
        return plantMapper.toResponseDtoList(plants);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlantResponseDto> getPlantsByCategory(UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new APIException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId);
        }
        List<Plant> plants = plantRepository.findByCategoryId(categoryId);
        return plantMapper.toResponseDtoList(plants);
    }

    @Override
    public PlantResponseDto createPlant(PlantRequestDto plantRequestDto) {
        Nursery nursery = nurseryRepository.findById(plantRequestDto.getNurseryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Nursery not found with id: " + plantRequestDto.getNurseryId()));

        Category category = categoryRepository.findById(plantRequestDto.getCategoryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category not found with id: " + plantRequestDto.getCategoryId()));

        if (plantRepository.existsByNurseryIdAndSku(plantRequestDto.getNurseryId(), plantRequestDto.getSku())) {
            throw new APIException(HttpStatus.CONFLICT, "A plant with SKU '" + plantRequestDto.getSku() + "' already exists for this nursery");
        }

        Plant plant = plantMapper.toEntity(plantRequestDto);
        plant.setNursery(nursery);
        plant.setCategory(category);

        Plant savedPlant = plantRepository.save(plant);
        return plantMapper.toResponseDto(savedPlant);
    }

    @Override
    public PlantResponseDto updatePlant(UUID id, PlantRequestDto plantRequestDto) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Plant not found with id: " + id));

        Nursery nursery = nurseryRepository.findById(plantRequestDto.getNurseryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Nursery not found with id: " + plantRequestDto.getNurseryId()));

        Category category = categoryRepository.findById(plantRequestDto.getCategoryId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category not found with id: " + plantRequestDto.getCategoryId()));

        if (!plant.getSku().equals(plantRequestDto.getSku()) || !plant.getNursery().getId().equals(plantRequestDto.getNurseryId())) {
            if (plantRepository.existsByNurseryIdAndSku(plantRequestDto.getNurseryId(), plantRequestDto.getSku())) {
                throw new APIException(HttpStatus.CONFLICT, "A plant with SKU '" + plantRequestDto.getSku() + "' already exists for this nursery");
            }
        }

        plant.setNursery(nursery);
        plant.setCategory(category);
        plant.setName(plantRequestDto.getName());
        plant.setScientificName(plantRequestDto.getScientificName());
        plant.setSku(plantRequestDto.getSku());
        plant.setDescription(plantRequestDto.getDescription());
        plant.setCareInstructions(plantRequestDto.getCareInstructions());
        plant.setPrice(plantRequestDto.getPrice());
        plant.setStock(plantRequestDto.getStock());
        plant.setPlantType(plantRequestDto.getPlantType());
        plant.setImageUrl(plantRequestDto.getImageUrl());
        plant.setActive(plantRequestDto.isActive());

        Plant updatedPlant = plantRepository.save(plant);
        return plantMapper.toResponseDto(updatedPlant);
    }

    @Override
    public void deletePlant(UUID id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Plant not found with id: " + id));
        plantRepository.delete(plant);
    }
}
