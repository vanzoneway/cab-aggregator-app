package com.modsen.driverservice.mapper;

import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.model.Driver;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


/**
 * Mapper interface for converting between {@link Driver} entities and {@link DriverDto} data transfer objects.
 * <p>
 * This interface defines methods for mapping properties between the two types, enabling
 * the transformation of data for various layers of the application.
 * </p>
 *
 * <p>
 * The mapping operations include:
 * </p>
 * <ul>
 *     <li>Conversion from {@link DriverDto} to {@link Driver} entity.</li>
 *     <li>Conversion from {@link Driver} entity to {@link DriverDto}.</li>
 *     <li>Partial updating of an existing {@link Driver} entity using a {@link DriverDto}.</li>
 * </ul>
 *
 * <pre>
 * Example usage:
 * DriverDto driverDto = new DriverDto();
 * Driver driver = driverMapper.toEntity(driverDto);
 * </pre>
 *
 * @see Driver
 * @see DriverDto
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface DriverMapper {

    Driver toEntity(DriverDto dto);

    DriverDto toDto(Driver driver);

    /**
     * Partially updates a {@link Driver} entity using the provided {@link DriverDto}.
     * <p>
     * The {@link BeanMapping} annotation allows customization of the mapping behavior.
     * This method is designed to update only the properties of the {@link Driver} entity
     * that are present in the {@link DriverDto}.
     * </p>
     *
     * <p>
     * The parameters used in {@link BeanMapping} are:
     * </p>
     * <ul>
     *     <li>{@code nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE}:
     *     This setting tells the mapper to ignore any null values from the source {@link DriverDto}
     *     when mapping to the target {@link Driver}. This allows existing values in the target
     *     entity to remain unchanged.</li>
     * </ul>
     *
     * The {@link MappingTarget} annotation indicates that the provided {@link Driver} entity
     * should be updated with the values from the {@link DriverDto}.
     *
     * @param driverDto the data transfer object containing updated properties
     * @param driver the Driver entity to update
     * @return the updated Driver entity
     */
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    Driver partialUpdate(DriverDto driverDto, @MappingTarget Driver driver);
}
