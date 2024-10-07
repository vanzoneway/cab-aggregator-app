package com.modsen.passengerservice.repository;

import com.modsen.passengerservice.model.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    boolean existsByEmailAndDeletedIsFalse(String email);

    boolean existsByPhoneAndDeletedIsFalse(String phone);

    boolean existsByEmailAndDeletedIsTrue(String email);

    boolean existsByPhoneAndDeletedIsTrue(String phone);

    Optional<Passenger> findByIdAndDeletedIsFalse(Long id);

    Page<Passenger> findAllByDeletedIsFalse(Pageable pageable);

}
