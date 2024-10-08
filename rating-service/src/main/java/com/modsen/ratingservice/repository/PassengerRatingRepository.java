package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.model.PassengerRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRatingRepository extends JpaRepository<PassengerRating, Long> {
}
