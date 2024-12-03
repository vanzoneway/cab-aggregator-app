package com.modsen.ratingservice.kafka;

import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerSender {

    private final KafkaTemplate<String, AverageRatingResponseDto> kafkaAverageRatingTemplate;

    public void sendAverageRatingToDriver(AverageRatingResponseDto averageRatingResponseDto) {
        kafkaAverageRatingTemplate.send(KafkaConstants.DRIVER_TOPIC_NAME, averageRatingResponseDto)
                .whenComplete(KafkaProducerSender::logResult);
    }

    public void sendAverageRatingToPassenger(AverageRatingResponseDto averageRatingResponseDto) {
        kafkaAverageRatingTemplate.send(KafkaConstants.PASSENGER_TOPIC_NAME, averageRatingResponseDto)
                .whenComplete(KafkaProducerSender::logResult);
    }

    private static void logResult(SendResult<String, AverageRatingResponseDto> result, Throwable exception) {
        if (exception != null) {
            log.error("Failed to send message: {}", exception.getMessage());
        } else {
            log.info("Message sent successfully to topic: {}, partition: {}, offset: {}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        }
    }

}