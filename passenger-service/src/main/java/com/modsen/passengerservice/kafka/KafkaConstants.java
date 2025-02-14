package com.modsen.passengerservice.kafka;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KafkaConstants {

    //KafkaConsumerConfig
    public static final String BOOTSTRAP_SERVERS_FROM_APP_YAML = "${spring.kafka.consumer.bootstrap-servers}";
    public static final String AUTO_OFFSET_RESET_CONFIG_VALUE = "earliest";
    public static final String TRUSTED_PACKAGES_VALUE = "*";

    //AverageRatingListener
    public static final String TOPIC_NAME_AVERAGE_RATING_FROM_RATING_SERVICE = "passenger-average-rating-topic";
    public static final String GROUP_ID_AVERAGE_RATING_FROM_RATING_SERVICE = "passenger-consumer";
    public static final String TARGET_DTO_FOR_DESERIALIZATION_PROPERTY =
            "spring.json.value.default.type=com.modsen.passengerservice.dto.AverageRatingResponseDto";

    //Common
    public static final String BASE_KAFKA_LISTENER_CONTAINER_FACTORY = "kafkaListenerContainerFactory";

}
