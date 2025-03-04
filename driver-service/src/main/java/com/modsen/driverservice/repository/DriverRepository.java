package com.modsen.driverservice.repository;

import com.modsen.driverservice.model.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"cars"})
    Optional<Driver> findByIdAndDeletedIsFalse(@NonNull Long id);

    boolean existsByEmailAndDeletedIsFalse(String email);

    boolean existsByPhoneAndDeletedIsFalse(String phone);

    boolean existsByEmailAndDeletedIsTrue(String email);

    boolean existsByPhoneAndDeletedIsTrue(String phone);

    Page<Driver> findAllByDeletedIsFalse(Pageable pageable);

    List<Driver> findAllByDeletedIsFalse();

    long countByDeletedIsFalse();


}
