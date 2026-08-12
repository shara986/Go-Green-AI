package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.WishlistItemRequestDto;
import com.gogreen.ai.dto.response.WishlistItemResponseDto;
import com.gogreen.ai.entity.Plant;
import com.gogreen.ai.entity.Wishlist;
import com.gogreen.ai.entity.WishlistItem;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-12T15:06:18+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.12 (Oracle Corporation)"
)
@Component
public class WishlistItemMapperImpl implements WishlistItemMapper {

    @Override
    public WishlistItemResponseDto toResponseDto(WishlistItem wishlistItem) {
        if ( wishlistItem == null ) {
            return null;
        }

        WishlistItemResponseDto wishlistItemResponseDto = new WishlistItemResponseDto();

        wishlistItemResponseDto.setWishlistId( wishlistItemWishlistId( wishlistItem ) );
        wishlistItemResponseDto.setPlantId( wishlistItemPlantId( wishlistItem ) );
        wishlistItemResponseDto.setPlantName( wishlistItemPlantName( wishlistItem ) );
        wishlistItemResponseDto.setPlantPrice( wishlistItemPlantPrice( wishlistItem ) );
        wishlistItemResponseDto.setId( wishlistItem.getId() );
        wishlistItemResponseDto.setCreatedAt( wishlistItem.getCreatedAt() );
        wishlistItemResponseDto.setUpdatedAt( wishlistItem.getUpdatedAt() );

        return wishlistItemResponseDto;
    }

    @Override
    public List<WishlistItemResponseDto> toResponseDtoList(List<WishlistItem> wishlistItems) {
        if ( wishlistItems == null ) {
            return null;
        }

        List<WishlistItemResponseDto> list = new ArrayList<WishlistItemResponseDto>( wishlistItems.size() );
        for ( WishlistItem wishlistItem : wishlistItems ) {
            list.add( toResponseDto( wishlistItem ) );
        }

        return list;
    }

    @Override
    public WishlistItem toEntity(WishlistItemRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        WishlistItem wishlistItem = new WishlistItem();

        return wishlistItem;
    }

    private UUID wishlistItemWishlistId(WishlistItem wishlistItem) {
        if ( wishlistItem == null ) {
            return null;
        }
        Wishlist wishlist = wishlistItem.getWishlist();
        if ( wishlist == null ) {
            return null;
        }
        UUID id = wishlist.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID wishlistItemPlantId(WishlistItem wishlistItem) {
        if ( wishlistItem == null ) {
            return null;
        }
        Plant plant = wishlistItem.getPlant();
        if ( plant == null ) {
            return null;
        }
        UUID id = plant.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String wishlistItemPlantName(WishlistItem wishlistItem) {
        if ( wishlistItem == null ) {
            return null;
        }
        Plant plant = wishlistItem.getPlant();
        if ( plant == null ) {
            return null;
        }
        String name = plant.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Double wishlistItemPlantPrice(WishlistItem wishlistItem) {
        if ( wishlistItem == null ) {
            return null;
        }
        Plant plant = wishlistItem.getPlant();
        if ( plant == null ) {
            return null;
        }
        Double price = plant.getPrice();
        if ( price == null ) {
            return null;
        }
        return price;
    }
}
