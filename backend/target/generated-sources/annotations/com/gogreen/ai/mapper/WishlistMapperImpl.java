package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.WishlistRequestDto;
import com.gogreen.ai.dto.response.WishlistResponseDto;
import com.gogreen.ai.entity.User;
import com.gogreen.ai.entity.Wishlist;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-01T22:27:55+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class WishlistMapperImpl implements WishlistMapper {

    @Autowired
    private WishlistItemMapper wishlistItemMapper;

    @Override
    public WishlistResponseDto toResponseDto(Wishlist wishlist) {
        if ( wishlist == null ) {
            return null;
        }

        WishlistResponseDto wishlistResponseDto = new WishlistResponseDto();

        wishlistResponseDto.setUserId( wishlistUserId( wishlist ) );
        wishlistResponseDto.setId( wishlist.getId() );
        wishlistResponseDto.setItems( wishlistItemMapper.toResponseDtoList( wishlist.getItems() ) );
        wishlistResponseDto.setCreatedAt( wishlist.getCreatedAt() );
        wishlistResponseDto.setUpdatedAt( wishlist.getUpdatedAt() );

        return wishlistResponseDto;
    }

    @Override
    public List<WishlistResponseDto> toResponseDtoList(List<Wishlist> wishlists) {
        if ( wishlists == null ) {
            return null;
        }

        List<WishlistResponseDto> list = new ArrayList<WishlistResponseDto>( wishlists.size() );
        for ( Wishlist wishlist : wishlists ) {
            list.add( toResponseDto( wishlist ) );
        }

        return list;
    }

    @Override
    public Wishlist toEntity(WishlistRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Wishlist wishlist = new Wishlist();

        return wishlist;
    }

    private UUID wishlistUserId(Wishlist wishlist) {
        if ( wishlist == null ) {
            return null;
        }
        User user = wishlist.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
