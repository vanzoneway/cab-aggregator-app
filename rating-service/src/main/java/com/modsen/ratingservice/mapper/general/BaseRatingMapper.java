package com.modsen.ratingservice.mapper.general;

import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface BaseRatingMapper<T> {

    T toRating(RatingRequestDto ratingRequestDto);

    @Mapping(target = "userType", defaultExpression = "java(userType)")
    RatingResponseDto toDto(T rating, String userType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(RatingRequestDto ratingRequestDto, @MappingTarget T rating);

}
