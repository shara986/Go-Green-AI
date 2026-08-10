package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.ReviewRequestDto;
import com.gogreen.ai.dto.response.ReviewResponseDto;
import com.gogreen.ai.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface ReviewMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "plant.id", target = "plantId")
    @Mapping(source = "plant.name", target = "plantName")
    ReviewResponseDto toResponseDto(Review review);

    List<ReviewResponseDto> toResponseDtoList(List<Review> reviews);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "plant", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Review toEntity(ReviewRequestDto dto);
}
