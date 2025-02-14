package com.modsen.driverservice.service.impl;

import com.modsen.driverservice.client.ride.RideFeignClient;
import com.modsen.driverservice.client.ride.dto.RideStatisticResponseDto;
import com.modsen.driverservice.constants.AppConstants;
import com.modsen.driverservice.kafka.producer.dto.GeneralUserStatisticDto;
import com.modsen.driverservice.kafka.producer.statistic.StatisticSender;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.model.Driver;
import com.modsen.driverservice.repository.DriverRepository;
import com.modsen.driverservice.service.StatisticReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticReportServiceImpl implements StatisticReportService {

    private static final String SCHEDULED_CRON = "0 0 0 1 * ?";

    private final RideFeignClient rideFeignClient;
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final StatisticSender statisticSender;

    @Override
    @Scheduled(cron = SCHEDULED_CRON)
    public void scheduledSenderStatisticToNotificationService() {
        List<Driver> drivers = driverRepository.findAllByDeletedIsFalse();
        drivers.forEach(driver -> {
            RideStatisticResponseDto rideStatisticResponseDto = rideFeignClient.getRideStatisticForDriver(
                    driver.getId());
            GeneralUserStatisticDto generalUserStatisticDto = GeneralUserStatisticDto.builder()
                    .withRideStatistic(rideStatisticResponseDto)
                    .withUser(driverMapper.toUserDto(driver))
                    .withUserType(AppConstants.USER_TYPE)
                    .build();
            statisticSender.sendGeneralUserStatisticToNotificationService(generalUserStatisticDto);
        });
    }

}
