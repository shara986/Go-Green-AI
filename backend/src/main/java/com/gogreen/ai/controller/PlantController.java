package com.gogreen.ai.controller;

import com.gogreen.ai.dto.ApiResponse;
import com.gogreen.ai.dto.request.PlantRequestDto;
import com.gogreen.ai.dto.response.PageResponseDto;
import com.gogreen.ai.dto.response.PlantResponseDto;
import com.gogreen.ai.service.PlantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plants")
@RequiredArgsConstructor
@Tag(name = "Plants", description = "Public & Management APIs for Plant Catalog")
public class PlantController {

    private final PlantService plantService;

    @GetMapping
    @Operation(summary = "Get all plants", description = "Retrieves paginated plant catalog with optional search query and active status filtering.")
    public ResponseEntity<ApiResponse<PageResponseDto<PlantResponseDto>>> getAllPlants(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        PageResponseDto<PlantResponseDto> plants = plantService.getAllPlants(search, active, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(new ApiResponse<>(true, "Plants retrieved successfully", plants));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get plant by ID", description = "Retrieves detail of a plant by its unique ID.")
    public ResponseEntity<ApiResponse<PlantResponseDto>> getPlantById(@PathVariable UUID id) {
        PlantResponseDto plant = plantService.getPlantById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Plant retrieved successfully", plant));
    }

    @GetMapping("/nursery/{nurseryId}")
    @Operation(summary = "Get plants by Nursery ID", description = "Retrieves all plants belonging to a specific nursery.")
    public ResponseEntity<ApiResponse<List<PlantResponseDto>>> getPlantsByNursery(@PathVariable UUID nurseryId) {
        List<PlantResponseDto> plants = plantService.getPlantsByNursery(nurseryId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Nursery plants retrieved successfully", plants));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get plants by Category ID", description = "Retrieves all plants in a specific category.")
    public ResponseEntity<ApiResponse<List<PlantResponseDto>>> getPlantsByCategory(@PathVariable UUID categoryId) {
        List<PlantResponseDto> plants = plantService.getPlantsByCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Category plants retrieved successfully", plants));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSERY_MANAGER')")
    @Operation(summary = "Create a new plant", description = "Creates a new plant listing (Admin & Nursery Manager only).")
    public ResponseEntity<ApiResponse<PlantResponseDto>> createPlant(@Valid @RequestBody PlantRequestDto plantRequestDto) {
        PlantResponseDto createdPlant = plantService.createPlant(plantRequestDto);
        return new ResponseEntity<>(new ApiResponse<>(true, "Plant created successfully", createdPlant), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSERY_MANAGER')")
    @Operation(summary = "Update a plant", description = "Updates an existing plant listing by ID (Admin & Nursery Manager only).")
    public ResponseEntity<ApiResponse<PlantResponseDto>> updatePlant(
            @PathVariable UUID id,
            @Valid @RequestBody PlantRequestDto plantRequestDto) {
        PlantResponseDto updatedPlant = plantService.updatePlant(id, plantRequestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Plant updated successfully", updatedPlant));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSERY_MANAGER')")
    @Operation(summary = "Delete a plant", description = "Deletes a plant listing by ID (Admin & Nursery Manager only).")
    public ResponseEntity<ApiResponse<String>> deletePlant(@PathVariable UUID id) {
        plantService.deletePlant(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Plant deleted successfully", "Plant deleted"));
    }
}
