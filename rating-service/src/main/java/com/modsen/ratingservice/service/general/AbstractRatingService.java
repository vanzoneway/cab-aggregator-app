package com.modsen.ratingservice.service.general;

import com.modsen.ratingservice.constants.AppConstants;
import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import com.modsen.ratingservice.exception.rating.RatingNotFoundException;
import com.modsen.ratingservice.exception.rating.DuplicateRideIdException;
import com.modsen.ratingservice.mapper.general.BaseRatingMapper;
import com.modsen.ratingservice.mapper.ListContainerMapper;
import com.modsen.ratingservice.model.general.Rating;
import com.modsen.ratingservice.repository.general.CommonRatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@AllArgsConstructor
public class AbstractRatingService<T extends Rating, R extends CommonRatingRepository<T>>
        implements CommonRatingService<T> {

    protected final R repository;
    protected final BaseRatingMapper<T> ratingMapper;
    protected final ListContainerMapper listContainerMapper;
    protected final MessageSource messageSource;

    private String userType;

    //TODO create sync interaction with rides-service
    @Override
    @Transactional
    public RatingResponseDto createRating(RatingRequestDto ratingRequestDto) {
        checkRatingExistsByRideId(ratingRequestDto);
        checkRatingRestoreOption(ratingRequestDto);
        T rating = ratingMapper.toRating(ratingRequestDto);
        rating.setDeleted(false);
        repository.save(rating);
        return ratingMapper.toDto(rating, userType);
    }

    @Override
    @Transactional
    public RatingResponseDto updateRatingById(Long id, RatingRequestDto ratingRequestDto) {
        checkRatingRestoreOption(ratingRequestDto);
        checkRatingExistsByRideId(ratingRequestDto);
        T rating = getDriverRating(id);
        ratingMapper.partialUpdate(ratingRequestDto, rating);
        repository.save(rating);
        return ratingMapper.toDto(rating, userType);
    }

    @Override
    public RatingResponseDto getRating(Long id) {
        T rating = getDriverRating(id);
        return ratingMapper.toDto(rating, userType);
    }

    @Override
    public ListContainerResponseDto<RatingResponseDto> getRatingsByRideId(Long rideId, Integer offset, Integer limit) {
        Page<RatingResponseDto> ratingsResponseDto = repository
                .findAllByRideIdAndDeletedIsFalse(rideId, PageRequest.of(offset, limit))
                .map(rating -> ratingMapper.toDto(rating, userType));
        return listContainerMapper.toDto(ratingsResponseDto);
    }

    @Override
    @Transactional
    public void safeDeleteRating(Long id) {
        T rating = getDriverRating(id);
        rating.setDeleted(true);
        repository.save(rating);
    }

    private void checkRatingRestoreOption(RatingRequestDto ratingRequestDto) {
        Long rideId = ratingRequestDto.rideId();
        if (Objects.nonNull(rideId) && repository.existsByRideIdAndDeletedIsTrue(rideId)) {
            throw new DuplicateRideIdException(messageSource.getMessage(
                    AppConstants.RESTORE_RATING_BY_RIDE_ID,
                    new Object[]{userType, rideId},
                    LocaleContextHolder.getLocale()
            ));
        }
    }

    private void checkRatingExistsByRideId(RatingRequestDto ratingRequestDto) {
        Long rideId = ratingRequestDto.rideId();
        if (Objects.nonNull(rideId) && repository.existsByRideIdAndDeletedIsFalse(rideId)) {
            throw new DuplicateRideIdException(messageSource.getMessage(
                    AppConstants.RATING_DUPLICATE_RIDE_ID_MESSAGE_KEY,
                    new Object[]{userType, rideId},
                    LocaleContextHolder.getLocale()
            ));
        }
    }

    private T getDriverRating(Long id) {
        return repository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new RatingNotFoundException(messageSource.getMessage(
                        AppConstants.RATING_NOT_FOUND_MESSAGE_KEY,
                        new Object[]{userType, id},
                        LocaleContextHolder.getLocale()
                )));
    }

}
