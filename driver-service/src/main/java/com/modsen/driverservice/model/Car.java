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


/**
 * Entity representing information about a car.
 * <p>
 * This class contains information about the brand, color, number, model, and year of manufacture of the car.
 * It also establishes a relationship with the driver who operates this car.
 * </p>
 *
 * <pre>
 * Example usage:
 * Car car = new Car();
 * car.setBrand("Toyota");
 * car.setColor("Red");
 * car.setNumber("AB123CD");
 * car.setModel("Camry");
 * car.setYear(2020);
 * </pre>
 *
 * @see com.modsen.driverservice.model.Car Car
 * @see com.modsen.driverservice.dto.CarDto CarDto
 * @see com.modsen.driverservice.mapper.CarMapper CarMapper
 */
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
    private String number;

    private String model;
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    Driver driver;
}
