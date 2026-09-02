package com.gogreen.ai.mapper;

import com.gogreen.ai.dto.request.ReviewRequestDto;
import com.gogreen.ai.dto.response.ReviewResponseDto;
import com.gogreen.ai.entity.Plant;
import com.gogreen.ai.entity.Review;
import com.gogreen.ai.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-02T07:58:26+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewResponseDto toResponseDto(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();

        reviewResponseDto.setUserId( reviewUserId( review ) );
        reviewResponseDto.setUserName( reviewUserName( review ) );
        reviewResponseDto.setPlantId( reviewPlantId( review ) );
        reviewResponseDto.setPlantName( reviewPlantName( review ) );
        reviewResponseDto.setComment( review.getComment() );
        reviewResponseDto.setCreatedAt( review.getCreatedAt() );
        reviewResponseDto.setId( review.getId() );
        reviewResponseDto.setRating( review.getRating() );
        reviewResponseDto.setUpdatedAt( review.getUpdatedAt() );

        return reviewResponseDto;
    }

    @Override
    public List<ReviewResponseDto> toResponseDtoList(List<Review> reviews) {
        if ( reviews == null ) {
            return null;
        }

        List<ReviewResponseDto> list = new ArrayList<ReviewResponseDto>( reviews.size() );
        for ( Review review : reviews ) {
            list.add( toResponseDto( review ) );
        }

        return list;
    }

    @Override
    public Review toEntity(ReviewRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Review review = new Review();

        review.setComment( dto.getComment() );
        review.setRating( dto.getRating() );

        return review;
    }

    private UUID reviewUserId(Review review) {
        if ( review == null ) {
            return null;
        }
        User user = review.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String reviewUserName(Review review) {
        if ( review == null ) {
            return null;
        }
        User user = review.getUser();
        if ( user == null ) {
            return null;
        }
        String name = user.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID reviewPlantId(Review review) {
        if ( review == null ) {
            return null;
        }
        Plant plant = review.getPlant();
        if ( plant == null ) {
            return null;
        }
        UUID id = plant.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String reviewPlantName(Review review) {
        if ( review == null ) {
            return null;
        }
        Plant plant = review.getPlant();
        if ( plant == null ) {
            return null;
        }
        String name = plant.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
