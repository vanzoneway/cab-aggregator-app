package com.modsen.driverservice.repository;

import com.modsen.driverservice.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
