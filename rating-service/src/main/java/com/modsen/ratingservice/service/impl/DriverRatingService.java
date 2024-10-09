package com.modsen.ratingservice.service.impl;

import com.modsen.ratingservice.constants.AppConstants;
import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.UserType;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import com.modsen.ratingservice.exception.rating.DriverRatingNotFoundException;
import com.modsen.ratingservice.exception.rating.DuplicateRideIdException;
import com.modsen.ratingservice.mapper.ListContainerMapper;
import com.modsen.ratingservice.mapper.RatingMapper;
import com.modsen.ratingservice.model.DriverRating;
import com.modsen.ratingservice.repository.DriverRatingRepository;
import com.modsen.ratingservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service("driverRatingService")
@RequiredArgsConstructor
public class DriverRatingService implements RatingService {

    private final DriverRatingRepository driverRatingRepository;
    private final RatingMapper ratingMapper;
    private final ListContainerMapper listContainerMapper;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public RatingResponseDto createRating(RatingRequestDto ratingRequestDto) {
        checkDriverRatingExistsByRideId(ratingRequestDto);
        checkDriverRatingRestoreOption(ratingRequestDto);
        DriverRating driverRating = ratingMapper.toDriverRating(ratingRequestDto);
        driverRating.setDeleted(false);
        driverRatingRepository.save(driverRating);
        return ratingMapper.toDto(driverRating, String.valueOf(UserType.DRIVER));
    }

    @Override
    @Transactional
    public RatingResponseDto updateRatingById(Long id, RatingRequestDto ratingRequestDto) {
        checkDriverRatingRestoreOption(ratingRequestDto);
        checkDriverRatingExistsByRideId(ratingRequestDto);
        DriverRating driverRating = getDriverRating(id);
        ratingMapper.partialUpdate(ratingRequestDto, driverRating);
        driverRatingRepository.save(driverRating);
        return ratingMapper.toDto(driverRating, String.valueOf(UserType.DRIVER));
    }

    @Override
    public RatingResponseDto getRating(Long id) {
        return ratingMapper.toDto(getDriverRating(id), String.valueOf(UserType.DRIVER));
    }

    @Override
    public ListContainerResponseDto<RatingResponseDto> getRatingsByRideId(Long rideId,Integer offset, Integer limit) {
        Page<RatingResponseDto> ratingsResponseDto = driverRatingRepository
                .findAllByRideIdAndDeletedIsFalse(rideId, PageRequest.of(offset, limit))
                .map(x -> ratingMapper.toDto(x, String.valueOf(UserType.DRIVER)));
        return listContainerMapper.toDto(ratingsResponseDto);
    }

    @Override
    @Transactional
    public void safeDeleteRating(Long id) {
        DriverRating driverRating = getDriverRating(id);
        driverRating.setDeleted(true);
        driverRatingRepository.save(driverRating);
    }

    private void checkDriverRatingRestoreOption(RatingRequestDto ratingRequestDto) {
        Long rideId = ratingRequestDto.rideId();
        if (Objects.nonNull(rideId) && driverRatingRepository.existsByRideIdAndDeletedIsTrue(rideId)) {
            throw new DuplicateRideIdException(messageSource.getMessage(
                    AppConstants.RESTORE_DRIVER_RATING_BY_RIDE_ID,
                    new Object[]{rideId},
                    LocaleContextHolder.getLocale()
            ));
        }
    }

    private void checkDriverRatingExistsByRideId(RatingRequestDto ratingRequestDto) {
        Long rideId = ratingRequestDto.rideId();
        if (Objects.nonNull(rideId) && driverRatingRepository.existsByRideIdAndDeletedIsFalse(rideId)) {
            throw new DuplicateRideIdException(messageSource.getMessage(
                    AppConstants.DRIVER_RATING_DUPLICATE_RIDE_ID_MESSAGE_KEY,
                    new Object[]{rideId},
                    LocaleContextHolder.getLocale()
            ));
        }
    }

    private DriverRating getDriverRating(Long id) {
        return driverRatingRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new DriverRatingNotFoundException(messageSource.getMessage(
                        AppConstants.DRIVER_RATING_NOT_FOUND_MESSAGE_KEY,
                        new Object[]{id},
                        LocaleContextHolder.getLocale()
                )));
    }

}
