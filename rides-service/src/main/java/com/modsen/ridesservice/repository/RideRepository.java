package com.modsen.ridesservice.repository;

import com.modsen.ridesservice.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    @NonNull
    Page<Ride> findAll(@NonNull Pageable pageable);

    @NonNull
    Page<Ride> findAllByDriverId(@NonNull Long driverId, @NonNull Pageable pageable);

    @NonNull
    Page<Ride> findAllByPassengerId(@NonNull Long passengerId, @NonNull Pageable pageable);

}
