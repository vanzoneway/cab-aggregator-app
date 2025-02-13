package com.modsen.driverservice.kafka.producer.statistic;

import com.modsen.driverservice.kafka.producer.dto.GeneralUserStatisticDto;
import com.modsen.driverservice.kafka.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StatisticSender {

    private final KafkaTemplate<String, GeneralUserStatisticDto> kafkaGeneralStatisticTemplate;

    public void sendGeneralUserStatisticToNotificationService(GeneralUserStatisticDto generalUserStatisticDto) {
        kafkaGeneralStatisticTemplate.send(KafkaConstants.GENERAL_USER_STATISTIC_TOPIC_NAME, generalUserStatisticDto)
                .whenComplete(StatisticSender::logResult);
    }

    private static void logResult(SendResult<String, GeneralUserStatisticDto> result, Throwable exception) {
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