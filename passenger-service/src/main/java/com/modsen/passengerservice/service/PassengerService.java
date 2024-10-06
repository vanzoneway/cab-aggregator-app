package com.modsen.passengerservice.service;

import com.modsen.passengerservice.dto.ListContainerResponseDto;
import com.modsen.passengerservice.dto.PassengerDto;

public interface PassengerService {

    PassengerDto createPassenger(PassengerDto passengerDto);

    ListContainerResponseDto<PassengerDto> getPagePassengers(Integer offset, Integer limit);

    void safeDeletePassengerById(Long passengerId);

    PassengerDto updatePassengerById(Long passengerId, PassengerDto passengerDto);

    PassengerDto getPassengerById(Long passengerId);

}
