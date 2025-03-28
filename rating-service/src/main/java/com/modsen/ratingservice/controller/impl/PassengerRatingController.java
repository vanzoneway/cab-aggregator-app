package com.modsen.ratingservice.controller.impl;

import com.modsen.ratingservice.constants.AppConstants;
import com.modsen.ratingservice.controller.general.PassengerRatingOperations;
import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import com.modsen.ratingservice.service.impl.PassengerRatingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/passengers-ratings")
@RequiredArgsConstructor
public class PassengerRatingController implements PassengerRatingOperations {

    private final PassengerRatingService passengerRatingService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('PASSENGER')")
    @CachePut(value = AppConstants.PASSENGER_RATING_CACHE_VALUE, key = "#result.id()")
    public RatingResponseDto createPassengerRating(@Valid @RequestBody RatingRequestDto ratingRequestDto) {
        return passengerRatingService.createRating(ratingRequestDto);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CachePut(value = AppConstants.PASSENGER_RATING_CACHE_VALUE, key = "#result.id()")
    public RatingResponseDto updatePassengerRating(@PathVariable Long id,
                                                   @Valid @RequestBody RatingRequestDto ratingRequestDto) {
        return passengerRatingService.updateRatingById(id, ratingRequestDto);
    }

    @Override
    @GetMapping("/{id}")
    @Cacheable(value = AppConstants.PASSENGER_RATING_CACHE_VALUE, key = "#id")
    public RatingResponseDto getPassengerRating(@PathVariable Long id) {
        return passengerRatingService.getRating(id);
    }

    @Override
    @GetMapping("/users/{refUserId}")
    public ListContainerResponseDto<RatingResponseDto> getPassengerRatingsByRefUserId(@PathVariable Long refUserId,
                                                                                      @RequestParam(defaultValue = "0")
                                                                                      @Min(0) Integer offset,
                                                                                      @RequestParam(defaultValue = "10")
                                                                                      @Min(1) @Max(100) Integer limit) {
        return passengerRatingService.getRatingsByRefUserId(refUserId, offset, limit);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = AppConstants.PASSENGER_RATING_CACHE_VALUE, key = "#id")
    public void deletePassengerRating(@PathVariable Long id) {
        passengerRatingService.safeDeleteRating(id);
    }

    @Override
    @GetMapping("/{refUserId}/average")
    @Cacheable(value = AppConstants.AVERAGE_PASSENGER_RATING_CACHE_VALUE, key = "#refUserId")
    public AverageRatingResponseDto averagePassengerRating(@PathVariable Long refUserId) {
        return passengerRatingService.getAverageRating(refUserId);
    }

}
