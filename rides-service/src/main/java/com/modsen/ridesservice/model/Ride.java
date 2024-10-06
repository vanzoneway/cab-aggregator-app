package com.modsen.ridesservice.model;

import com.modsen.ridesservice.model.enums.RideStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long driverId;

    @Column(nullable = false)
    private Long passengerId;

    @Column(nullable = false)
    private String departureAddress;

    @Column(nullable = false)
    private String destinationAddress;

    @Enumerated(EnumType.STRING)
    private RideStatus rideStatus;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = false, precision = 7, scale = 2)
    private BigDecimal cost;

}
