package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.NurseryRequestDto;
import com.gogreen.ai.dto.response.NurseryResponseDto;
import com.gogreen.ai.dto.response.PageResponseDto;

import java.util.UUID;

public interface NurseryService {

    PageResponseDto<NurseryResponseDto> getAllNurseries(String search, int pageNo, int pageSize, String sortBy, String sortDir);

    NurseryResponseDto getNurseryById(UUID id);

    NurseryResponseDto getNurseryByUserId(UUID userId);

    NurseryResponseDto registerNursery(NurseryRequestDto nurseryRequestDto);

    NurseryResponseDto updateNursery(UUID id, NurseryRequestDto nurseryRequestDto);
}
