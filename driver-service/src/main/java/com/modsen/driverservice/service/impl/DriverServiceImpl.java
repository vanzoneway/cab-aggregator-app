package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.dto.ListContainerResponseDto;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.exception.driver.DuplicateDriverEmailPhoneException;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.mapper.ListContainerMapper;
import com.modsen.driverservice.model.Driver;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final MessageSource messageSource;
    private final DriverMapper driverMapper;
    private final ListContainerMapper listContainerMapper;

    @Override
    public DriverDto createDriver(DriverDto driverDto) {
        if (driverRepository.existsByPhoneAndDeletedIsFalse(driverDto.phone())) {
            throw new DuplicateDriverEmailPhoneException(messageSource.getMessage(
                    "driver.phone.duplicate",
                    new Object[]{driverDto.phone()},
                    LocaleContextHolder.getLocale()));
        }

        if (driverRepository.existsByEmailAndDeletedIsFalse(driverDto.email())) {
            throw new DuplicateDriverEmailPhoneException(messageSource.getMessage(
                    "driver.email.duplicate",
                    new Object[]{driverDto.email()},
                    LocaleContextHolder.getLocale()));
        }

        Driver driverEntity = driverMapper.toEntity(driverDto);
        driverEntity.setDeleted(false);
        driverEntity = driverRepository.save(driverEntity);

        return driverMapper.toDto(driverEntity);
    }

    @Override
    public ListContainerResponseDto<DriverDto> getPageDrivers(Integer offset, Integer limit) {
        Page<DriverDto> driversPageDto = driverRepository
                .findAllByDeletedIsFalse(PageRequest.of(offset, limit))
                .map(driverMapper::toDto);
        return listContainerMapper.toDto(driversPageDto);
    }

    @Override
    public void safeDeleteDriverByDriverId(Long driverId) {
        Driver driverEntity = getDriverByIdAndDeletedIsFalse(driverId);
        driverEntity.setDeleted(true);
        driverRepository.save(driverEntity);
    }

    @Override
    public DriverDto updateDriverById(Long driverId, DriverDto driverDto) {
        Driver driverEntity = getDriverByIdAndDeletedIsFalse(driverId);
        driverMapper.partialUpdate(driverDto, driverEntity);
        driverRepository.save(driverEntity);
        return driverMapper.toDto(driverEntity);
    }

    @Override
    public DriverDto getDriverById(Long driverId) {
        return driverMapper.toDto(getDriverByIdAndDeletedIsFalse(driverId));
    }

    private Driver getDriverByIdAndDeletedIsFalse(Long driverId) {
        return driverRepository.findByIdAndDeletedIsFalse(driverId)
                .orElseThrow(() -> new DriverNotFoundException(messageSource.getMessage(
                        "driver.not.found",
                        new Object[]{driverId},
                        LocaleContextHolder.getLocale())));
    }

}