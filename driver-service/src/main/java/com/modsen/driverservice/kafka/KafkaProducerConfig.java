package com.modsen.driverservice.kafka;

import com.modsen.driverservice.kafka.producer.dto.GeneralUserStatisticDto;
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

    private static final int AMOUNT_OF_REPLICAS = 3;
    private static final int AMOUNT_OF_PARTITIONS = 3;

    @Value(KafkaConstants.BOOTSTRAP_SERVERS_FROM_APP_YAML)
    private List<String> bootstrapAddress;

    @Bean
    public ProducerFactory<String, GeneralUserStatisticDto> userStatisticProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, GeneralUserStatisticDto> kafkaTemplate() {
        KafkaTemplate<String, GeneralUserStatisticDto> kafkaTemplate =
                new KafkaTemplate<>(userStatisticProducerFactory());
        kafkaTemplate.setObservationEnabled(true);
        return kafkaTemplate;
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic generalUserStatisticTopic() {
        return TopicBuilder.name(KafkaConstants.GENERAL_USER_STATISTIC_TOPIC_NAME)
                .replicas(AMOUNT_OF_REPLICAS)
                .partitions(AMOUNT_OF_PARTITIONS)
                .build();
    }

}