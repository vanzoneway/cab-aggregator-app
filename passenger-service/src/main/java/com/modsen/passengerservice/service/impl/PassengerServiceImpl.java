package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.dto.ListContainerResponseDto;
import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.service.PassengerService;
import org.springframework.stereotype.Service;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Override
    public PassengerDto createPassenger(PassengerDto passengerDto) {
        return null;
    }

    @Override
    public ListContainerResponseDto<PassengerDto> getPagePassengers(Integer offset, Integer limit) {
        return null;
    }

    @Override
    public void safeDeletePassengerByPassengerId(Long passengerId) {

    }

    @Override
    public PassengerDto updatePassengerById(Long passengerId, PassengerDto passengerDto) {
        return null;
    }

    @Override
    public PassengerDto getPassengerById(Long passengerId) {
        return null;
    }

}
