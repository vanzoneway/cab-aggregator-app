package com.modsen.passengerservice.mapper;

import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.model.Passenger;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface PassengerMapper {

    PassengerDto toDto(Passenger passenger);

    Passenger toEntity(PassengerDto passengerDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(PassengerDto passengerDto, @MappingTarget Passenger passenger);

}
