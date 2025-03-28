package com.modsen.ridesservice.repository;

import com.modsen.ridesservice.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    @NonNull
    Page<Ride> findAll(@NonNull Pageable pageable);

    @NonNull
    Page<Ride> findAllByDriverId(@NonNull Long driverId, @NonNull Pageable pageable);

    @NonNull
    Page<Ride> findAllByPassengerId(@NonNull Long passengerId, @NonNull Pageable pageable);

    @Query("""
            SELECT r\s
            FROM Ride r\s
            WHERE r.driverId = :driverId\s
              AND r.rideStatus <> 'CANCELED'\s
              AND r.orderDateTime >= CURRENT_DATE - 1 MONTH
            """)
    List<Ride> findAllRidesForReportByDriverId(@Param("driverId") @NonNull Long driverId);

    @Query("""
                SELECT r\s
                FROM Ride r\s
                WHERE r.passengerId = :passengerId\s
                  AND r.rideStatus <> 'CANCELED'\s
                  AND r.orderDateTime >= CURRENT_DATE - 1 MONTH\s
            """)
    List<Ride> findAllRidesForReportByPassengerId(@Param("passengerId") @NonNull Long passengerId);

}
