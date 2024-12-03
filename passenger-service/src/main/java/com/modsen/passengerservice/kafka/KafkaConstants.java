package com.modsen.passengerservice.kafka;

public class KafkaConstants {

    public static final String BOOTSTRAP_SERVERS_FROM_APP_YAML = "${spring.kafka.consumer.bootstrap-servers}";
    public static final String PACKAGE_NAME_AVERAGE_RATING_FROM_RATING_SERVICE
            = "com.modsen.ratingservice.dto.response.AverageRatingResponseDto";
    public static final String PACKAGE_NAME_AVERAGE_RATING_FROM_PASSENGER_SERVICE =
            "com.modsen.passengerservice.dto.AverageRatingResponseDto";
    public static final String TOPIC_NAME_AVERAGE_RATING_FROM_RATING_SERVICE = "passenger-average-rating-topic";

}
