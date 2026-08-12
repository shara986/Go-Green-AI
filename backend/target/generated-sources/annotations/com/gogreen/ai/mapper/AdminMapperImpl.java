package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.response.AdminCategoryResponseDto;
import com.gogreen.ai.entity.Category;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-12T15:06:18+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.12 (Oracle Corporation)"
)
@Component
public class AdminMapperImpl implements AdminMapper {

    @Override
    public AdminCategoryResponseDto toAdminCategoryResponseDto(Category category) {
        if ( category == null ) {
            return null;
        }

        AdminCategoryResponseDto adminCategoryResponseDto = new AdminCategoryResponseDto();

        adminCategoryResponseDto.setSlug( category.getSlug() );
        adminCategoryResponseDto.setActive( category.isActive() );
        adminCategoryResponseDto.setId( category.getId() );
        adminCategoryResponseDto.setName( category.getName() );
        adminCategoryResponseDto.setDescription( category.getDescription() );
        adminCategoryResponseDto.setIcon( category.getIcon() );
        adminCategoryResponseDto.setCreatedAt( category.getCreatedAt() );
        adminCategoryResponseDto.setUpdatedAt( category.getUpdatedAt() );

        return adminCategoryResponseDto;
    }

    @Override
    public List<AdminCategoryResponseDto> toAdminCategoryResponseDtoList(List<Category> categories) {
        if ( categories == null ) {
            return null;
        }

        List<AdminCategoryResponseDto> list = new ArrayList<AdminCategoryResponseDto>( categories.size() );
        for ( Category category : categories ) {
            list.add( toAdminCategoryResponseDto( category ) );
        }

        return list;
    }
}
