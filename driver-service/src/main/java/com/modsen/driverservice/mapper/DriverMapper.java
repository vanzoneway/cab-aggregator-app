package com.modsen.driverservice.mapper;

import com.modsen.driverservice.dto.AverageRatingResponseDto;
import com.modsen.driverservice.dto.DriverCarDto;
import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.kafka.producer.dto.UserDto;
import com.modsen.driverservice.model.Driver;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        builder = @Builder(disableBuilder = true))
public interface DriverMapper {

    @Mapping(target = "id", ignore = true)
    Driver toEntity(DriverDto dto);

    DriverDto toDto(Driver driver);

    DriverCarDto toDriverCarDto(Driver driver);

    UserDto toUserDto(Driver driver);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(DriverDto driverDto, @MappingTarget Driver driver);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(AverageRatingResponseDto averageRatingResponseDto, @MappingTarget Driver driver);

}
