package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.WishlistRequestDto;
import com.gogreen.ai.dto.response.WishlistResponseDto;
import com.gogreen.ai.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfigBase.class, uses = WishlistItemMapper.class)
public interface WishlistMapper {

    @Mapping(source = "user.id", target = "userId")
    WishlistResponseDto toResponseDto(Wishlist wishlist);

    List<WishlistResponseDto> toResponseDtoList(List<Wishlist> wishlists);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Wishlist toEntity(WishlistRequestDto dto);
}
