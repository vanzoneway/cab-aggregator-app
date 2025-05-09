package com.modsen.ratingservice.controller.impl;

import com.modsen.ratingservice.constants.AppConstants;
import com.modsen.ratingservice.controller.general.DriverRatingOperations;
import com.modsen.ratingservice.dto.ListContainerResponseDto;
import com.modsen.ratingservice.dto.request.RatingRequestDto;
import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import com.modsen.ratingservice.dto.response.RatingResponseDto;
import com.modsen.ratingservice.service.impl.DriverRatingService;
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
@RequestMapping("/api/v1/drivers-ratings")
@RequiredArgsConstructor
public class DriverRatingController implements DriverRatingOperations {

    private final DriverRatingService driverRatingService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER')")
    @CachePut(value = AppConstants.DRIVER_RATING_CACHE_VALUE, key = "#result.id()")
    public RatingResponseDto createDriverRating(@Valid @RequestBody RatingRequestDto ratingRequestDto) {
        return driverRatingService.createRating(ratingRequestDto);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CachePut(value = AppConstants.DRIVER_RATING_CACHE_VALUE, key ="#result.id()")
    public RatingResponseDto updateDriverRating(@PathVariable Long id,
                                                @Valid @RequestBody RatingRequestDto ratingRequestDto) {
        return driverRatingService.updateRatingById(id, ratingRequestDto);
    }

    @Override
    @GetMapping("/{id}")
    @Cacheable(value = AppConstants.DRIVER_RATING_CACHE_VALUE, key = "#id")
    public RatingResponseDto getDriverRating(@PathVariable Long id) {
        return driverRatingService.getRating(id);
    }

    @Override
    @GetMapping("/users/{refUserId}")
    public ListContainerResponseDto<RatingResponseDto> getDriverRatingsByRefUserId(@PathVariable Long refUserId,
                                                                                   @RequestParam(defaultValue = "0")
                                                                                   @Min(0) Integer offset,
                                                                                   @RequestParam(defaultValue = "10")
                                                                                   @Min(1) @Max(100) Integer limit) {
        return driverRatingService.getRatingsByRefUserId(refUserId, offset, limit);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = AppConstants.DRIVER_RATING_CACHE_VALUE, key = "#id")
    public void deleteDriverRating(@PathVariable Long id) {
        driverRatingService.safeDeleteRating(id);
    }

    @Override
    @GetMapping("/{refUserId}/average")
    @Cacheable(value = AppConstants.AVERAGE_DRIVER_RATING_CACHE_VALUE, key = "#refUserId")
    public AverageRatingResponseDto averageDriverRating(@PathVariable Long refUserId) {
        return driverRatingService.getAverageRating(refUserId);
    }

}
