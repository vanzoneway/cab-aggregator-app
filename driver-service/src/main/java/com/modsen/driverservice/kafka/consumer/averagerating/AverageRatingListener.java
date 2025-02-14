package com.modsen.driverservice.kafka.consumer.averagerating;

import com.modsen.driverservice.constants.AppConstants;
import com.modsen.driverservice.dto.AverageRatingResponseDto;
import com.modsen.driverservice.exception.driver.DriverNotFoundException;
import com.modsen.driverservice.kafka.KafkaConstants;
import com.modsen.driverservice.mapper.DriverMapper;
import com.modsen.driverservice.model.Driver;
import com.modsen.driverservice.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AverageRatingListener {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final MessageSource messageSource;

    @KafkaListener(
            topics = KafkaConstants.TOPIC_NAME_AVERAGE_RATING_FROM_RATING_SERVICE,
            groupId = KafkaConstants.GROUP_ID_AVERAGE_RATING_FROM_RATING_SERVICE,
            containerFactory = KafkaConstants.BASE_KAFKA_LISTENER_CONTAINER_FACTORY,
            properties = {KafkaConstants.TARGET_DTO_FOR_DESERIALIZATION_PROPERTY})
    public void listenToDriverAverageRating(AverageRatingResponseDto averageRatingResponseDto) {
        Driver driver = driverRepository.findByIdAndDeletedIsFalse(averageRatingResponseDto.refUserId())
                .orElseThrow(() -> new DriverNotFoundException(messageSource.getMessage(
                        AppConstants.DRIVER_NOT_FOUND_MESSAGE_KEY,
                        new Object[]{averageRatingResponseDto.refUserId()},
                        LocaleContextHolder.getLocale())));
        driverMapper.partialUpdate(averageRatingResponseDto, driver);
        driverRepository.save(driver);
        log.info("Received message from driver topic: {}", averageRatingResponseDto);
    }

}
