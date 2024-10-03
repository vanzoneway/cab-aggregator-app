package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.constants.AppConstants;
import com.modsen.driverservice.dto.DriverCarDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final MessageSource messageSource;
    private final DriverMapper driverMapper;
    private final ListContainerMapper listContainerMapper;

    @Override
    @Transactional
    public DriverDto createDriver(DriverDto driverDto) {
        checkDriverRestoreOption(driverDto);
        checkCarExistsByPhoneOrEmail(driverDto);
        Driver driverEntity = driverMapper.toEntity(driverDto);
        driverEntity.setDeleted(false);

        return driverMapper.toDto(driverRepository.save(driverEntity));
    }

    @Override
    public ListContainerResponseDto<DriverDto> getPageDrivers(Integer offset, Integer limit) {
        Page<DriverDto> driversPageDto = driverRepository
                .findAllByDeletedIsFalse(PageRequest.of(offset, limit))
                .map(driverMapper::toDto);
        return listContainerMapper.toDto(driversPageDto);
    }

    @Override
    @Transactional
    public void safeDeleteDriverByDriverId(Long driverId) {
        Driver driverEntity = getDriverByIdAndDeletedIsFalse(driverId);
        driverEntity.setDeleted(true);
        driverRepository.save(driverEntity);
    }

    @Override
    @Transactional
    public DriverDto updateDriverById(Long driverId, DriverDto driverDto) {
        checkDriverRestoreOption(driverDto);
        checkCarExistsByPhoneOrEmail(driverDto);
        Driver driverEntity = getDriverByIdAndDeletedIsFalse(driverId);
        driverMapper.partialUpdate(driverDto, driverEntity);
        driverRepository.save(driverEntity);
        return driverMapper.toDto(driverEntity);
    }

    @Override
    public DriverDto getDriverById(Long driverId) {
        return driverMapper.toDto(getDriverByIdAndDeletedIsFalse(driverId));
    }

    @Override
    public DriverCarDto getDriverWithCars(Long driverId) {
        Driver driverEntity = driverRepository.findByIdAndDeletedIsFalse(driverId)
                .orElseThrow(() -> new DriverNotFoundException(messageSource.getMessage(
                        AppConstants.DRIVER_NOT_FOUND_MESSAGE_KEY,
                        new Object[]{driverId},
                        LocaleContextHolder.getLocale())));

        return driverMapper.toDriverCarDto(driverEntity);
    }

    private void checkCarExistsByPhoneOrEmail(DriverDto driverDto) {
        if (driverRepository.existsByPhoneAndDeletedIsFalse(driverDto.phone())) {
            throw new DuplicateDriverEmailPhoneException(messageSource.getMessage(
                    AppConstants.DRIVER_PHONE_DUPLICATE_MESSAGE_KEY,
                    new Object[]{driverDto.phone()},
                    LocaleContextHolder.getLocale()));
        }

        if (driverRepository.existsByEmailAndDeletedIsFalse(driverDto.email())) {
            throw new DuplicateDriverEmailPhoneException(messageSource.getMessage(
                    AppConstants.DRIVER_EMAIL_DUPLICATE_MESSAGE_KEY,
                    new Object[]{driverDto.email()},
                    LocaleContextHolder.getLocale()));
        }
    }

    private void checkDriverRestoreOption(DriverDto driverDto) {
        String driverEmail = driverDto.email();
        String driverPhone = driverDto.phone();
        if (Objects.nonNull(driverEmail) && driverRepository.existsByEmailAndDeletedIsTrue(driverEmail)) {
            throw new DuplicateDriverEmailPhoneException(messageSource.getMessage(
                    AppConstants.RESTORE_DRIVER_BY_EMAIL_MESSAGE_KEY,
                    new Object[]{driverEmail},
                    LocaleContextHolder.getLocale()));
        }
        if (Objects.nonNull(driverPhone) && driverRepository.existsByPhoneAndDeletedIsTrue(driverPhone)) {
            throw new DuplicateDriverEmailPhoneException(messageSource.getMessage(
                    AppConstants.RESTORE_DRIVER_BY_PHONE_MESSAGE_KEY,
                    new Object[]{driverPhone},
                    LocaleContextHolder.getLocale()));
        }
    }

    private Driver getDriverByIdAndDeletedIsFalse(Long driverId) {
        return driverRepository.findByIdAndDeletedIsFalse(driverId)
                .orElseThrow(() -> new DriverNotFoundException(messageSource.getMessage(
                        AppConstants.DRIVER_NOT_FOUND_MESSAGE_KEY,
                        new Object[]{driverId},
                        LocaleContextHolder.getLocale())));
    }

}