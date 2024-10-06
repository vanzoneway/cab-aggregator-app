package com.modsen.passengerservice.service.impl;

import com.modsen.passengerservice.constants.AppConstants;
import com.modsen.passengerservice.dto.ListContainerResponseDto;
import com.modsen.passengerservice.dto.PassengerDto;
import com.modsen.passengerservice.exception.passenger.DuplicatePassengerPhoneOrEmailException;
import com.modsen.passengerservice.exception.passenger.PassengerNotFoundException;
import com.modsen.passengerservice.mapper.ListContainerMapper;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.model.Passenger;
import com.modsen.passengerservice.repository.PassengerRepository;
import com.modsen.passengerservice.service.PassengerService;
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
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;
    private final ListContainerMapper listContainerMapper;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public PassengerDto createPassenger(PassengerDto passengerDto) {
        checkPassengerExistsByPhoneOrEmail(passengerDto);
        checkPassengerRestoreOption(passengerDto);
        Passenger passenger = passengerMapper.toEntity(passengerDto);
        passenger.setDeleted(false);
        passengerRepository.save(passenger);
        return passengerMapper.toDto(passenger);
    }

    @Override
    public ListContainerResponseDto<PassengerDto> getPagePassengers(Integer offset, Integer limit) {
        Page<PassengerDto> passengersPageDto = passengerRepository
                .findAllByDeletedIsFalse(PageRequest.of(offset, limit))
                .map(passengerMapper::toDto);
        return listContainerMapper.toDto(passengersPageDto);
    }

    @Override
    @Transactional
    public void safeDeletePassengerById(Long passengerId) {
        Passenger passenger = getPassengerByIdAndDeletedIsFalse(passengerId);
        passenger.setDeleted(true);
        passengerRepository.save(passenger);
    }

    @Override
    @Transactional
    public PassengerDto updatePassengerById(Long passengerId, PassengerDto passengerDto) {
        checkPassengerRestoreOption(passengerDto);
        checkPassengerExistsByPhoneOrEmail(passengerDto);
        Passenger passenger = getPassengerByIdAndDeletedIsFalse(passengerId);
        passengerMapper.partialUpdate(passengerDto, passenger);
        passenger.setDeleted(false);
        passengerRepository.save(passenger);
        return passengerMapper.toDto(passenger);
    }

    @Override
    public PassengerDto getPassengerById(Long passengerId) {
        return passengerMapper.toDto(getPassengerByIdAndDeletedIsFalse(passengerId));
    }

    private void checkPassengerExistsByPhoneOrEmail(PassengerDto passengerDto) {
        String passengerEmail = passengerDto.email();
        if (Objects.nonNull(passengerEmail) && passengerRepository.existsByEmailAndDeletedIsFalse(passengerEmail)) {
            throw new DuplicatePassengerPhoneOrEmailException(messageSource.getMessage(
                    AppConstants.PASSENGER_EMAIL_DUPLICATE_MESSAGE_KEY,
                    new Object[]{passengerEmail},
                    LocaleContextHolder.getLocale()
            ));
        }
        String passengerPhone = passengerDto.phone();
        if (Objects.nonNull(passengerPhone) && passengerRepository.existsByPhoneAndDeletedIsFalse(passengerPhone)) {
            throw new DuplicatePassengerPhoneOrEmailException(messageSource.getMessage(
                    AppConstants.PASSENGER_PHONE_DUPLICATE_MESSAGE_KEY,
                    new Object[]{passengerPhone},
                    LocaleContextHolder.getLocale()
            ));
        }
    }

    private void checkPassengerRestoreOption(PassengerDto passengerDto) {
        String passengerEmail = passengerDto.email();
        if (Objects.nonNull(passengerEmail) && passengerRepository.existsByEmailAndDeletedIsTrue(passengerEmail)) {
            throw new DuplicatePassengerPhoneOrEmailException(messageSource.getMessage(
                    AppConstants.RESTORE_PASSENGER_BY_EMAIL_MESSAGE_KEY,
                    new Object[]{passengerEmail},
                    LocaleContextHolder.getLocale()
            ));
        }
        String passengerPhone = passengerDto.phone();
        if (Objects.nonNull(passengerPhone) && passengerRepository.existsByPhoneAndDeletedIsTrue(passengerPhone)) {
            throw new DuplicatePassengerPhoneOrEmailException(messageSource.getMessage(
                    AppConstants.RESTORE_PASSENGER_BY_PHONE_MESSAGE_KEY,
                    new Object[]{passengerPhone},
                    LocaleContextHolder.getLocale()
            ));
        }
    }

    private Passenger getPassengerByIdAndDeletedIsFalse(Long passengerId) {
        return passengerRepository.findByIdAndDeletedIsFalse(passengerId)
                .orElseThrow(() -> new PassengerNotFoundException(messageSource.getMessage(
                        AppConstants.PASSENGER_NOT_FOUND_MESSAGE_KEY,
                        new Object[]{passengerId},
                        LocaleContextHolder.getLocale()
                )));
    }

}
