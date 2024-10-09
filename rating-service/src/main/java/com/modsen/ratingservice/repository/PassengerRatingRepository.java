package com.modsen.ratingservice.repository;

import com.modsen.ratingservice.model.PassengerRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRatingRepository extends JpaRepository<PassengerRating, Long> {

    boolean existsByRideIdAndDeletedIsFalse(Long rideId);

    boolean existsByRideIdAndDeletedIsTrue(Long rideId);

    Optional<PassengerRating> findByIdAndDeletedIsFalse(Long id);

    Page<PassengerRating> findAllByRideIdAndDeletedIsFalse(Long rideId, Pageable pageable);

}
