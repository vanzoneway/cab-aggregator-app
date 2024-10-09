package com.modsen.ratingservice.mapper.impl;

import com.modsen.ratingservice.mapper.general.BaseRatingMapper;
import com.modsen.ratingservice.model.DriverRating;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface DriverRatingMapper extends BaseRatingMapper<DriverRating> {
}
