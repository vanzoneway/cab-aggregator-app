package com.modsen.driverservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String brand;

    private String color;

    @Column(unique = true)
    private String number;

    private String model;

    private Integer year;

    @Column(nullable = false)
    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

}
