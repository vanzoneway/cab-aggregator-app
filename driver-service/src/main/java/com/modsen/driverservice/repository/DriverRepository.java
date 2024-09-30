package com.modsen.driverservice.repository;

import com.modsen.driverservice.model.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @NonNull
    Optional<Driver> findByIdAndDeletedIsFalse(@NonNull Long id);

    Boolean existsByEmailAndDeletedIsFalse(String email);

    Boolean existsByPhoneAndDeletedIsFalse(String phone);

    Page<Driver> findAllByDeletedIsFalse(Pageable pageable);

}
