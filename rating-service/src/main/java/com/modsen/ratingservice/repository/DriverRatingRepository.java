package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.model.DriverRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRatingRepository extends JpaRepository<DriverRating, Long> {
}
