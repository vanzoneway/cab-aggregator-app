package com.modsen.ridesservice.service.impl;

import com.modsen.ridesservice.constants.AppConstants;
import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import com.modsen.ridesservice.exception.ride.RideNotFoundException;
import com.modsen.ridesservice.mapper.ListContainerMapper;
import com.modsen.ridesservice.mapper.RideMapper;
import com.modsen.ridesservice.model.Ride;
import com.modsen.ridesservice.repository.RideRepository;
import com.modsen.ridesservice.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final ListContainerMapper listContainerMapper;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public RideResponseDto createRide(RideRequestDto rideRequestDto) {
        Ride ride = rideMapper.toEntity(rideRequestDto);
        ride.setOrderDateTime(LocalDateTime.now());
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    @Transactional
    public RideResponseDto changeStatusRide(Long rideId, RideStatusRequestDto rideStatusRequestDto) {
        Ride ride = getRide(rideId);
        rideMapper.partialUpdate(rideStatusRequestDto, ride);
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    @Transactional
    public RideResponseDto updateRide(Long rideId, RideRequestDto rideRequestDto) {
        Ride ride = getRide(rideId);
        rideMapper.partialUpdate(rideRequestDto, ride);
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    public RideResponseDto getRideById(Long rideId) {
        Ride ride = getRide(rideId);
        return rideMapper.toDto(ride);
    }

    @Override
    public ListContainerResponseDto<RideResponseDto> getPageRides(Integer offset, Integer limit) {
        Page<RideResponseDto> rideResponsePageDto = rideRepository
                .findAll(PageRequest.of(offset, limit))
                .map(rideMapper::toDto);
        return listContainerMapper.toDto(rideResponsePageDto);
    }

    private Ride getRide(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(messageSource.getMessage(
                        AppConstants.RIDE_NOT_FOUND_MESSAGE_KEY,
                        new Object[]{rideId},
                        LocaleContextHolder.getLocale())));
    }

}
