package com.modsen.driverservice.repository;

import com.modsen.driverservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    Boolean existsByNumberAndDeletedIsFalse(String number);

    Optional<Car> findByIdAndDeletedIsFalse(@NonNull Long id);

}
