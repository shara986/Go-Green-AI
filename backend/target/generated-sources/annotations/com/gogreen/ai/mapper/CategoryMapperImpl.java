package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.CategoryRequestDto;
import com.gogreen.ai.dto.response.CategoryResponseDto;
import com.gogreen.ai.entity.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-12T14:58:57+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryResponseDto toResponseDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();

        categoryResponseDto.setParentCategoryId( categoryParentCategoryId( category ) );
        categoryResponseDto.setParentCategoryName( categoryParentCategoryName( category ) );
        categoryResponseDto.setActive( category.isActive() );
        categoryResponseDto.setCreatedAt( category.getCreatedAt() );
        categoryResponseDto.setDescription( category.getDescription() );
        categoryResponseDto.setIcon( category.getIcon() );
        categoryResponseDto.setId( category.getId() );
        categoryResponseDto.setImageUrl( category.getImageUrl() );
        categoryResponseDto.setName( category.getName() );
        categoryResponseDto.setSlug( category.getSlug() );
        categoryResponseDto.setUpdatedAt( category.getUpdatedAt() );

        return categoryResponseDto;
    }

    @Override
    public List<CategoryResponseDto> toResponseDtoList(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<CategoryResponseDto> list = new ArrayList<CategoryResponseDto>( categories.size() );
        for ( Category category : categories ) {
            list.add( toResponseDto( category ) );
        }

        return list;
    }

    @Override
    public Category toEntity(CategoryRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Category category = new Category();

        category.setDescription( dto.getDescription() );
        category.setImageUrl( dto.getImageUrl() );
        category.setName( dto.getName() );
        category.setSlug( dto.getSlug() );

        return category;
    }

    private UUID categoryParentCategoryId(Category category) {
        if ( category == null ) {
            return null;
        }
        Category parentCategory = category.getParentCategory();
        if ( parentCategory == null ) {
            return null;
        }
        UUID id = parentCategory.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String categoryParentCategoryName(Category category) {
        if ( category == null ) {
            return null;
        }
        Category parentCategory = category.getParentCategory();
        if ( parentCategory == null ) {
            return null;
        }
        String name = parentCategory.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
