package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.model.DriverRating;
import com.modsen.ratingservice.repository.general.CommonRatingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRatingRepository extends CommonRatingRepository<DriverRating> {
}
