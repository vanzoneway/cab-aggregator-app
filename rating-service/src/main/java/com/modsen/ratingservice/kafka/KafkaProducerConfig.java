package com.modsen.ratingservice.kafka;

import com.modsen.ratingservice.dto.response.AverageRatingResponseDto;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value(KafkaConstants.BOOTSTRAP_ADDRESS_FROM_YAML)
    private List<String> bootstrapAddress;

    @Bean
    public ProducerFactory<String, AverageRatingResponseDto> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, AverageRatingResponseDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic driverAverageRatingTopic() {
        return TopicBuilder.name(KafkaConstants.DRIVER_TOPIC_NAME)
                .replicas(3)
                .partitions(2)
                .build();
    }

    @Bean
    public NewTopic passengerAverageRatingTopic() {
        return TopicBuilder.name(KafkaConstants.PASSENGER_TOPIC_NAME)
                .replicas(3)
                .partitions(2)
                .build();
    }

}