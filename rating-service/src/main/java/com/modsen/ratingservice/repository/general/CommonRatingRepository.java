package com.modsen.ratingservice.repository.general;

import com.modsen.ratingservice.model.general.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface CommonRatingRepository<T extends Rating> extends JpaRepository<T, Long> {

    boolean existsByRideIdAndDeletedIsFalse(Long rideId);

    boolean existsByRideIdAndDeletedIsTrue(Long rideId);

    Optional<T> findByIdAndDeletedIsFalse(Long id);

    Page<T> findAllByRideIdAndDeletedIsFalse(Long rideId, Pageable pageable);

}
