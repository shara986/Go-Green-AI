package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.WishlistItemRequestDto;
import com.gogreen.ai.dto.response.WishlistItemResponseDto;
import com.gogreen.ai.entity.WishlistItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class)
public interface WishlistItemMapper {

    @Mapping(source = "wishlist.id", target = "wishlistId")
    @Mapping(source = "plant.id", target = "plantId")
    @Mapping(source = "plant.name", target = "plantName")
    @Mapping(source = "plant.price", target = "plantPrice")
    WishlistItemResponseDto toResponseDto(WishlistItem wishlistItem);

    List<WishlistItemResponseDto> toResponseDtoList(List<WishlistItem> wishlistItems);

    @Mapping(target = "wishlist", ignore = true)
    @Mapping(target = "plant", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    WishlistItem toEntity(WishlistItemRequestDto dto);
}
