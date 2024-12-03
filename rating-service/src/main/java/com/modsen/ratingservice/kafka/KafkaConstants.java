package com.modsen.ratingservice.kafka;

public class KafkaConstants {

    public static final String DRIVER_TOPIC_NAME = "driver-average-rating-topic";
    public static final String PASSENGER_TOPIC_NAME = "passenger-average-rating-topic";
    public static final String BOOTSTRAP_ADDRESS_FROM_YAML = "${spring.kafka.producer.bootstrap-servers}";

}
