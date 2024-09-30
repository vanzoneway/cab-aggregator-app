package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.dto.DriverDto;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.exception.driver.DuplicateDriverEmailPhoneException;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.model.Driver;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Override
    public DriverDto createDriver(DriverDto driverDto) {

        if (driverRepository.existsByPhoneAndDeletedIsFalse(driverDto.phone()))
            throw new DuplicateDriverEmailPhoneException(String.format("Phone %s already exists", driverDto.phone()));

        if(driverRepository.existsByEmailAndDeletedIsFalse(driverDto.email()))
            throw new DuplicateDriverEmailPhoneException(String.format("Email %s already exists", driverDto.email()));

        Driver driverEntity = driverMapper.toEntity(driverDto);
        driverEntity.setDeleted(false);
        driverEntity = driverRepository.save(driverEntity);

        return driverMapper.toDto(driverEntity);
    }

    @Override
    public Page<DriverDto> getPageDrivers(Integer offset, Integer limit) {
        return driverRepository
                .findAllByDeletedIsFalse(PageRequest.of(offset, limit))
                .map(driverMapper::toDto);
    }

    @Override
    public String safeDeleteDriverByDriverId(Long driverId) {
        Driver driverEntity = driverRepository.findByIdAndDeletedIsFalse(driverId)
                .orElseThrow(() -> new DriverNotFoundException(String.format("Driver %s not found", driverId)));
        driverEntity.setDeleted(true);
        driverRepository.save(driverEntity);
        return String.format("Driver %s deleted", driverId);
    }

    @Override
    public DriverDto updateDriverById(Long driverId, DriverDto driverDto) {
        Driver driverEntity = driverRepository.findByIdAndDeletedIsFalse(driverId)
                .orElseThrow(() -> new DriverNotFoundException(String.format("Driver %s not found", driverId)));

        driverMapper.partialUpdate(driverDto, driverEntity);
        driverRepository.save(driverEntity);
        return driverMapper.toDto(driverEntity);
    }


}
