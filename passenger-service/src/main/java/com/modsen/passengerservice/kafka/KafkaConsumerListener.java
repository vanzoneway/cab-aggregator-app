package com.modsen.passengerservice.kafka;

import com.modsen.passengerservice.constants.AppConstants;
import com.modsen.passengerservice.dto.AverageRatingResponseDto;
import com.modsen.passengerservice.exception.passenger.PassengerNotFoundException;
import com.modsen.passengerservice.mapper.PassengerMapper;
import com.modsen.passengerservice.model.Passenger;
import com.modsen.passengerservice.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerListener {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;
    private final MessageSource messageSource;

    @KafkaListener(topics = KafkaConstants.TOPIC_NAME_AVERAGE_RATING_FROM_RATING_SERVICE, groupId = "passenger-consumer",
            containerFactory = "kafkaAverageRatingListenerContainerFactory")
    public void listenToDriverAverageRating(AverageRatingResponseDto averageRatingResponseDto) {
        Passenger passenger = passengerRepository.findByIdAndDeletedIsFalse(averageRatingResponseDto.refUserId())
                .orElseThrow(() -> new PassengerNotFoundException(messageSource.getMessage(
                        AppConstants.PASSENGER_NOT_FOUND_MESSAGE_KEY,
                        new Object[]{averageRatingResponseDto.refUserId()},
                        LocaleContextHolder.getLocale())));
        passengerMapper.partialUpdate(averageRatingResponseDto, passenger);
        passengerRepository.save(passenger);
        log.info("Received message from driver topic: {}", averageRatingResponseDto);
    }

}
