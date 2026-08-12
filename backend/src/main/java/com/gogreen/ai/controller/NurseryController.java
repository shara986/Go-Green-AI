package com.gogreen.ai.controller;

import com.gogreen.ai.dto.ApiResponse;
import com.gogreen.ai.dto.request.NurseryRequestDto;
import com.gogreen.ai.dto.response.NurseryResponseDto;
import com.gogreen.ai.dto.response.PageResponseDto;
import com.gogreen.ai.service.NurseryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/nurseries")
@RequiredArgsConstructor
@Tag(name = "Nurseries", description = "Public & Management APIs for Plant Nurseries")
public class NurseryController {

    private final NurseryService nurseryService;

    @GetMapping
    @Operation(summary = "Get all nurseries", description = "Retrieves paginated list of nurseries with optional search query by name, city, or email.")
    public ResponseEntity<ApiResponse<PageResponseDto<NurseryResponseDto>>> getAllNurseries(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        PageResponseDto<NurseryResponseDto> nurseries = nurseryService.getAllNurseries(search, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(new ApiResponse<>(true, "Nurseries retrieved successfully", nurseries));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get nursery by ID", description = "Retrieves detail of a nursery by its ID.")
    public ResponseEntity<ApiResponse<NurseryResponseDto>> getNurseryById(@PathVariable UUID id) {
        NurseryResponseDto nursery = nurseryService.getNurseryById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Nursery retrieved successfully", nursery));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get nursery by User ID", description = "Retrieves nursery associated with a specific user ID.")
    public ResponseEntity<ApiResponse<NurseryResponseDto>> getNurseryByUserId(@PathVariable UUID userId) {
        NurseryResponseDto nursery = nurseryService.getNurseryByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "User nursery retrieved successfully", nursery));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'NURSERY_MANAGER', 'ADMIN')")
    @Operation(summary = "Register a new nursery", description = "Submits a nursery registration request.")
    public ResponseEntity<ApiResponse<NurseryResponseDto>> registerNursery(@Valid @RequestBody NurseryRequestDto nurseryRequestDto) {
        NurseryResponseDto registeredNursery = nurseryService.registerNursery(nurseryRequestDto);
        return new ResponseEntity<>(new ApiResponse<>(true, "Nursery registration submitted successfully", registeredNursery), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('NURSERY_MANAGER', 'ADMIN')")
    @Operation(summary = "Update nursery profile", description = "Updates profile details for an existing nursery.")
    public ResponseEntity<ApiResponse<NurseryResponseDto>> updateNursery(
            @PathVariable UUID id,
            @Valid @RequestBody NurseryRequestDto nurseryRequestDto) {
        NurseryResponseDto updatedNursery = nurseryService.updateNursery(id, nurseryRequestDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Nursery profile updated successfully", updatedNursery));
    }
}
