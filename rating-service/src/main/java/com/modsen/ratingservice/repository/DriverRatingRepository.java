package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.model.DriverRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRatingRepository extends JpaRepository<DriverRating, Long> {

    boolean existsByRideIdAndDeletedIsFalse(Long rideId);

    boolean existsByRideIdAndDeletedIsTrue(Long rideId);

    Optional<DriverRating> findByIdAndDeletedIsFalse(Long id);

    Page<DriverRating> findAllByRideIdAndDeletedIsFalse(Long rideId, Pageable pageable);

}
