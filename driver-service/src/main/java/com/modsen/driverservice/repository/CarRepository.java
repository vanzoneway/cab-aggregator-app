package com.modsen.driverservice.repository;

import com.modsen.driverservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
