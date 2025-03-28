package com.modsen.notificationservice.kafka.consumer;

import com.modsen.notificationservice.dto.statistic.GeneralUserStatisticDto;
import com.modsen.notificationservice.kafka.KafkaConstants;
import com.modsen.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class GeneralStatisticListener {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = KafkaConstants.GENERAL_USER_STATISTIC_TOPIC_NAME,
            groupId = KafkaConstants.GROUP_ID_GENERAL_USER_STATISTIC,
            containerFactory = KafkaConstants.BASE_KAFKA_LISTENER_CONTAINER_FACTORY,
            properties = {KafkaConstants.TARGET_DTO_FOR_DESERIALIZATION_PROPERTY})
    public void listenToDriverAverageRating(GeneralUserStatisticDto generalUserStatisticDto) {
        log.info("Received message from driver topic: {}", generalUserStatisticDto);
        notificationService.sendUserStatisticOnEmail(generalUserStatisticDto);
    }

}
