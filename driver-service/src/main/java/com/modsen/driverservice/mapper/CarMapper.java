package com.modsen.driverservice.mapper;

import com.modsen.driverservice.dto.CarDto;
import com.modsen.driverservice.model.Car;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


/**
 * Mapper interface for converting between {@link Car} entities and {@link CarDto} data transfer objects.
 * <p>
 * This interface defines methods for mapping properties between the two types, enabling
 * the transformation of data for various layers of the application.
 * </p>
 *
 * <p>
 * The mapping operations include:
 * </p>
 * <ul>
 *     <li>Conversion from {@link CarDto} to {@link Car} entity.</li>
 *     <li>Conversion from {@link Car} entity to {@link CarDto}.</li>
 *     <li>Partial updating of an existing {@link Car} entity using a {@link CarDto}.</li>
 * </ul>
 *
 * <pre>
 * Example usage:
 * CarDto carDto = new CarDto();
 * Car car = carMapper.toEntity(carDto);
 * </pre>
 *
 * @see Car
 * @see CarDto
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface CarMapper {

    Car toEntity(CarDto carDto);

    CarDto toDto(Car car);

    /**
     * Partially updates a {@link Car} entity using the provided {@link CarDto}.
     * <p>
     * The {@link BeanMapping} annotation allows customization of the mapping behavior.
     * This method is designed to update only the properties of the {@link Car} entity
     * that are present in the {@link CarDto}.
     * </p>
     *
     * <p>
     * The parameters used in {@link BeanMapping} are:
     * </p>
     * <ul>
     *     <li>{@code nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE}:
     *     This setting tells the mapper to ignore any null values from the source {@link CarDto}
     *     when mapping to the target {@link Car}. This allows existing values in the target
     *     entity to remain unchanged.</li>
     * </ul>
     *
     * The {@link MappingTarget} annotation indicates that the provided {@link Car} entity
     * should be updated with the values from the {@link CarDto}.
     *
     * @param carDto the data transfer object containing updated properties
     * @param car the Car entity to update
     * @return the updated Car entity
     */
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    Car partialUpdate(CarDto carDto, @MappingTarget Car car);

}
