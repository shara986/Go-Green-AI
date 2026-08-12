package com.gogreen.ai.service.impl;

import com.gogreen.ai.dto.request.NurseryRequestDto;
import com.gogreen.ai.dto.response.NurseryResponseDto;
import com.gogreen.ai.dto.response.PageResponseDto;
import com.gogreen.ai.entity.Nursery;
import com.gogreen.ai.entity.User;
import com.gogreen.ai.entity.enums.NurseryApprovalStatus;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.NurseryMapper;
import com.gogreen.ai.repository.NurseryRepository;
import com.gogreen.ai.repository.UserRepository;
import com.gogreen.ai.service.NurseryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NurseryServiceImpl implements NurseryService {

    private final NurseryRepository nurseryRepository;
    private final UserRepository userRepository;
    private final NurseryMapper nurseryMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<NurseryResponseDto> getAllNurseries(String search, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Nursery> nurseries = nurseryRepository.searchNurseries(search, pageable);

        PageResponseDto<NurseryResponseDto> pageResponse = new PageResponseDto<>();
        pageResponse.setContent(nurseryMapper.toResponseDtoList(nurseries.getContent()));
        pageResponse.setPage(nurseries.getNumber());
        pageResponse.setSize(nurseries.getSize());
        pageResponse.setTotalElements(nurseries.getTotalElements());
        pageResponse.setTotalPages(nurseries.getTotalPages());

        return pageResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public NurseryResponseDto getNurseryById(UUID id) {
        Nursery nursery = nurseryRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Nursery not found with id: " + id));
        return nurseryMapper.toResponseDto(nursery);
    }

    @Override
    @Transactional(readOnly = true)
    public NurseryResponseDto getNurseryByUserId(UUID userId) {
        Nursery nursery = nurseryRepository.findByUserId(userId)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Nursery not found for user id: " + userId));
        return nurseryMapper.toResponseDto(nursery);
    }

    @Override
    public NurseryResponseDto registerNursery(NurseryRequestDto nurseryRequestDto) {
        User user = userRepository.findById(nurseryRequestDto.getUserId())
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "User not found with id: " + nurseryRequestDto.getUserId()));

        if (nurseryRepository.existsByUserId(nurseryRequestDto.getUserId())) {
            throw new APIException(HttpStatus.CONFLICT, "A nursery is already registered for user with id: " + nurseryRequestDto.getUserId());
        }

        Nursery nursery = nurseryMapper.toEntity(nurseryRequestDto);
        nursery.setUser(user);
        nursery.setApprovalStatus(NurseryApprovalStatus.PENDING);
        nursery.setVerified(false);

        Nursery savedNursery = nurseryRepository.save(nursery);
        return nurseryMapper.toResponseDto(savedNursery);
    }

    @Override
    public NurseryResponseDto updateNursery(UUID id, NurseryRequestDto nurseryRequestDto) {
        Nursery nursery = nurseryRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Nursery not found with id: " + id));

        nursery.setName(nurseryRequestDto.getName());
        nursery.setDescription(nurseryRequestDto.getDescription());
        nursery.setAddress(nurseryRequestDto.getAddress());
        nursery.setCity(nurseryRequestDto.getCity());
        nursery.setPostalCode(nurseryRequestDto.getPostalCode());
        nursery.setContactEmail(nurseryRequestDto.getContactEmail());
        nursery.setContactPhone(nurseryRequestDto.getContactPhone());
        nursery.setLogoUrl(nurseryRequestDto.getLogoUrl());

        Nursery updatedNursery = nurseryRepository.save(nursery);
        return nurseryMapper.toResponseDto(updatedNursery);
    }
}
