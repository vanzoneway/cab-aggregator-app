package com.modsen.ratingservice.mapper;

import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import com.modsen.ratingservice.model.DriverRating;
import com.modsen.ratingservice.model.PassengerRating;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface RatingMapper {

    DriverRating toDriverRating (RatingRequestDto ratingRequestDto);

    PassengerRating toPassengerRating (RatingRequestDto ratingRequestDto);

    @Mapping(target = "userType", defaultExpression = "java(userType)")
    RatingResponseDto toDto (DriverRating driverRating, String userType);

    @Mapping(target = "userType", defaultExpression = "java(userType)")
    RatingResponseDto toDto (PassengerRating passengerRating, String userType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(RatingRequestDto ratingRequestDto, @MappingTarget DriverRating driverRating);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(RatingRequestDto ratingRequestDto, @MappingTarget PassengerRating passengerRating);

}
