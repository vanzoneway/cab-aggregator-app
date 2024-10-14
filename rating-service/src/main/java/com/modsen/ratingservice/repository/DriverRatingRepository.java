package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.model.DriverRating;
import com.modsen.ratingservice.repository.general.CommonRatingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRatingRepository extends CommonRatingRepository<DriverRating> {

    @Query("SELECT AVG(r.rating) FROM DriverRating r WHERE r.refUserId = :refUserId AND r.deleted = false")
    Optional<Double> getAverageRatingByRefUserId(@Param("refUserId") Long refUserId);

}
