package com.modsen.ridesservice.service.impl;

import com.modsen.ridesservice.constants.AppConstants;
import com.modsen.ridesservice.dto.ListContainerResponseDto;
import com.modsen.ridesservice.dto.request.RideRequestDto;
import com.modsen.ridesservice.dto.request.RideStatusRequestDto;
import com.modsen.ridesservice.dto.response.RideResponseDto;
import com.modsen.ridesservice.dto.response.RideStatisticResponseDto;
import com.modsen.ridesservice.exception.ride.RideNotFoundException;
import com.modsen.ridesservice.mapper.ListContainerMapper;
import com.modsen.ridesservice.mapper.RideMapper;
import com.modsen.ridesservice.model.Ride;
import com.modsen.ridesservice.model.enums.RideStatus;
import com.modsen.ridesservice.repository.RideRepository;
import com.modsen.ridesservice.service.RideService;
import com.modsen.ridesservice.service.component.RideServicePriceGenerator;
import com.modsen.ridesservice.service.component.RideServiceValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final ListContainerMapper listContainerMapper;
    private final MessageSource messageSource;
    private final RideServicePriceGenerator priceGenerator;
    private final RideServiceValidation rideServiceValidation;

    @Override
    @Transactional
    public RideResponseDto createRide(RideRequestDto rideRequestDto) {
        rideServiceValidation.checkExistingPassengerOrDriver(rideRequestDto);
        Ride ride = rideMapper.toEntity(rideRequestDto);
        ride.setRideStatus(RideStatus.CREATED);
        ride.setCost(priceGenerator.generateRandomCost());
        ride.setOrderDateTime(LocalDateTime.now());
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    @Transactional
    @CachePut(value = AppConstants.RIDE_CACHE_VALUE,
            condition = "#result.rideStatus() == 'CANCELED' || #result.rideStatus() == 'COMPLETED'",
            key = "#result.id()")
    public RideResponseDto changeRideStatus(Long rideId, RideStatusRequestDto rideStatusRequestDto) {
        Ride ride = getRide(rideId);
        rideServiceValidation.validateChangingRideStatus(ride, rideStatusRequestDto);
        rideMapper.partialUpdate(rideStatusRequestDto, ride);
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    @Transactional
    @CachePut(value = AppConstants.RIDE_CACHE_VALUE,
            condition = "#result.rideStatus() == 'CANCELED' || #result.rideStatus() == 'COMPLETED'",
            key = "#result.id()")
    public RideResponseDto updateRide(Long rideId, RideRequestDto rideRequestDto) {
        rideServiceValidation.checkExistingPassengerOrDriver(rideRequestDto);
        Ride ride = getRide(rideId);
        rideMapper.partialUpdate(rideRequestDto, ride);
        rideRepository.save(ride);
        return rideMapper.toDto(ride);
    }

    @Override
    @Cacheable(value = AppConstants.RIDE_CACHE_VALUE,
            unless = "#result.rideStatus() != 'CANCELED' && #result.rideStatus() != 'COMPLETED'",
            key = "#rideId")
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

    @Override
    public ListContainerResponseDto<RideResponseDto> getPageRidesByDriverId(Long driverId,
                                                                            Integer offset,
                                                                            Integer limit) {
        Page<RideResponseDto> rideResponsePageDto = rideRepository
                .findAllByDriverId(driverId, PageRequest.of(offset, limit))
                .map(rideMapper::toDto);
        return listContainerMapper.toDto(rideResponsePageDto);
    }

    @Override
    public ListContainerResponseDto<RideResponseDto> getPageRidesByPassengerId(Long passengerId,
                                                                               Integer offset,
                                                                               Integer limit) {
        Page<RideResponseDto> rideResponsePageDto = rideRepository
                .findAllByPassengerId(passengerId, PageRequest.of(offset, limit))
                .map(rideMapper::toDto);
        return listContainerMapper.toDto(rideResponsePageDto);
    }

    @Override
    public RideStatisticResponseDto getRideStatisticForDriver(Long driverId) {
        List<Ride> rides = rideRepository.findAllRidesForReportByDriverId(driverId);
        return getRideStatistic(rides, driverId);
    }

    @Override
    public RideStatisticResponseDto getRideStatisticForPassenger(Long passengerId) {
        List<Ride> rides = rideRepository.findAllRidesForReportByPassengerId(passengerId);
        return getRideStatistic(rides, passengerId);
    }

    private RideStatisticResponseDto getRideStatistic(List<Ride> rides, Long refUserId) {
        List<RideResponseDto> ridesResponseDto = rides
                .stream()
                .map(rideMapper::toDto)
                .toList();
        if (ridesResponseDto.isEmpty()) {
            return RideStatisticResponseDto.builder()
                    .withRides(ridesResponseDto)
                    .withUserRefId(refUserId)
                    .withAverageCost(BigDecimal.ZERO)
                    .build();
        }
        BigDecimal averageCost = ridesResponseDto
                .stream()
                .map(RideResponseDto::cost)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(ridesResponseDto.size()), RoundingMode.HALF_UP);
        return RideStatisticResponseDto.builder()
                .withRides(ridesResponseDto)
                .withUserRefId(refUserId)
                .withAverageCost(averageCost)
                .build();
    }

    private Ride getRide(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(messageSource.getMessage(
                        AppConstants.RIDE_NOT_FOUND_MESSAGE_KEY,
                        new Object[]{rideId},
                        LocaleContextHolder.getLocale())));
    }

}
