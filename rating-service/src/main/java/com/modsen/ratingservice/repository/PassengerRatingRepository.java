package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.model.PassengerRating;
import com.modsen.ratingservice.repository.general.CommonRatingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRatingRepository extends CommonRatingRepository<PassengerRating> {
}
