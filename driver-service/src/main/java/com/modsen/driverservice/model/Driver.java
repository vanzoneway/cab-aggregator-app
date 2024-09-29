package com.modsen.driverservice.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * Entity representing information about a driver.
 * <p>
 * This class contains information about the driver's name, email, phone number, age, and gender.
 * It also establishes a one-to-many relationship with the cars that the driver operates.
 * </p>
 *
 * <pre>
 * Example usage:
 * Driver driver = new Driver();
 * driver.setName("John Doe");
 * driver.setEmail("john.doe@example.com");
 * driver.setPhone("+1234567890");
 * driver.setAge(30);
 * driver.setGender("Male");
 * </pre>
 *
 * @see com.modsen.driverservice.model.Driver Driver
 * @see com.modsen.driverservice.dto.DriverDto DriverDto
 * @see com.modsen.driverservice.mapper.DriverMapper DriverMapper
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private Integer age;

    private String gender;

    @OneToMany(mappedBy = "driver",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    List<Car> cars;

}
