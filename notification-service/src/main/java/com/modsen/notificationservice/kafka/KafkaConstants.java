package com.modsen.notificationservice.kafka;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KafkaConstants {

    //KafkaConsumerConfig
    public static final String BOOTSTRAP_SERVERS_FROM_APP_YAML = "${spring.kafka.consumer.bootstrap-servers}";
    public static final String AUTO_OFFSET_RESET_CONFIG_VALUE = "earliest";
    public static final String TRUSTED_PACKAGES_VALUE = "*";
    public static final String BASE_KAFKA_LISTENER_CONTAINER_FACTORY = "kafkaListenerContainerFactory";

    //GeneralStatisticConsumerConfig
    public static final String GENERAL_USER_STATISTIC_TOPIC_NAME = "user-statistics-topic";
    public static final String GROUP_ID_GENERAL_USER_STATISTIC = "notification-consumer";
    public static final String TARGET_DTO_FOR_DESERIALIZATION_PROPERTY =
            "spring.json.value.default.type=com.modsen.notificationservice.dto.statistic.GeneralUserStatisticDto";

}
