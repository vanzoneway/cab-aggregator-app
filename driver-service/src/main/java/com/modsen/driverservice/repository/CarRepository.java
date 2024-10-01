package com.modsen.driverservice.repository;

import com.modsen.driverservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    boolean existsByNumberAndDeletedIsFalse(String number);

    Optional<Car> findByIdAndDeletedIsFalse(@NonNull Long id);

}
