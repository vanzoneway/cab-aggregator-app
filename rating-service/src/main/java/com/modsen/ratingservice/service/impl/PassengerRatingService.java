package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.constants.AppConstants;
import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.UserType;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import com.modsen.ratingservice.exception.rating.DuplicateRideIdException;
import com.modsen.ratingservice.exception.rating.PassengerRatingNotFoundException;
import com.modsen.ratingservice.mapper.ListContainerMapper;
import com.modsen.ratingservice.mapper.RatingMapper;
import com.modsen.ratingservice.model.DriverRating;
import com.modsen.ratingservice.model.PassengerRating;
import com.modsen.ratingservice.repository.DriverRatingRepository;
import com.modsen.ratingservice.repository.PassengerRatingRepository;
import com.modsen.ratingservice.service.RatingService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Objects;

@Service("passengerRatingService")
@RequiredArgsConstructor
public class PassengerRatingService implements RatingService {

    private final PassengerRatingRepository passengerRatingRepository;
    private final RatingMapper ratingMapper;
    private final ListContainerMapper listContainerMapper;
    private final MessageSource messageSource;

    @Override
    public RatingResponseDto createRating(RatingRequestDto ratingRequestDto) {
        checkPassengerRatingRestoreOption(ratingRequestDto);
        checkPassengerRatingRatingExistsByRideId(ratingRequestDto);
        PassengerRating passengerRating = ratingMapper.toPassengerRating(ratingRequestDto);
        passengerRating.setDeleted(false);
        passengerRatingRepository.save(passengerRating);
        return ratingMapper.toDto(passengerRating, String.valueOf(UserType.PASSENGER));
    }

    @Override
    @Transactional
    public RatingResponseDto updateRatingById(Long id, RatingRequestDto ratingRequestDto) {
        checkPassengerRatingRestoreOption(ratingRequestDto);
        checkPassengerRatingRatingExistsByRideId(ratingRequestDto);
        PassengerRating passengerRating = getPassengerRating(id);
        ratingMapper.partialUpdate(ratingRequestDto, passengerRating);
        passengerRatingRepository.save(passengerRating);
        return ratingMapper.toDto(passengerRating, String.valueOf(UserType.PASSENGER));
    }

    @Override
    public RatingResponseDto getRating(Long id) {
        return ratingMapper.toDto(getPassengerRating(id), String.valueOf(UserType.PASSENGER));
    }

    @Override
    public ListContainerResponseDto<RatingResponseDto> getRatingsByRideId(Long rideId, Integer offset, Integer limit) {
        Page<RatingResponseDto> ratingsResponseDto = passengerRatingRepository
                .findAllByRideIdAndDeletedIsFalse(rideId, PageRequest.of(offset, limit))
                .map(x -> ratingMapper.toDto(x, String.valueOf(UserType.PASSENGER)));
        return listContainerMapper.toDto(ratingsResponseDto);
    }

    @Override
    @Transactional
    public void safeDeleteRating(Long id) {
        PassengerRating passengerRating = getPassengerRating(id);
        passengerRating.setDeleted(true);
        passengerRatingRepository.save(passengerRating);
    }

    private void checkPassengerRatingRestoreOption(RatingRequestDto ratingRequestDto) {
        Long rideId = ratingRequestDto.rideId();
        if(Objects.nonNull(rideId) &&  passengerRatingRepository.existsByRideIdAndDeletedIsTrue(rideId)) {
            throw new DuplicateRideIdException(messageSource.getMessage(
                    AppConstants.RESTORE_PASSENGER_RATING_BY_RIDE_ID,
                    new Object[] {rideId},
                    LocaleContextHolder.getLocale()
            ));
        }
    }

    private void checkPassengerRatingRatingExistsByRideId(RatingRequestDto ratingRequestDto) {
        Long rideId = ratingRequestDto.rideId();
        if(Objects.nonNull(rideId) && passengerRatingRepository.existsByRideIdAndDeletedIsFalse(rideId)) {
            throw new DuplicateRideIdException(messageSource.getMessage(
                    AppConstants.PASSENGER_RATING_DUPLICATE_RIDE_ID_MESSAGE_KEY,
                    new Object[]{rideId},
                    LocaleContextHolder.getLocale()
            ));
        }
    }

    private PassengerRating getPassengerRating(Long id) {
        return passengerRatingRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new PassengerRatingNotFoundException(messageSource.getMessage(
                        AppConstants.PASSENGER_RATING_NOT_FOUND_MESSAGE_KEY,
                        new Object[]{id},
                        LocaleContextHolder.getLocale()
                )));
    }

}
