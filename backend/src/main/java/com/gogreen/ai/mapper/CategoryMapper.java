package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.CategoryRequestDto;
import com.gogreen.ai.dto.response.CategoryResponseDto;
import com.gogreen.ai.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface CategoryMapper {

    @Mapping(source = "parentCategory.id", target = "parentCategoryId")
    @Mapping(source = "parentCategory.name", target = "parentCategoryName")
    CategoryResponseDto toResponseDto(Category category);

    List<CategoryResponseDto> toResponseDtoList(List<Category> categories);

    @Mapping(target = "parentCategory", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryRequestDto dto);
}
