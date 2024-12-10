package com.example.demo.config;

import com.example.demo.kafka.TaskDeserializer;
import com.example.demo.model.dto.TaskUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProp.class)
public class KafkaConsumerConfig {
    private final KafkaProp kafkaProperty;

    @Bean
    public ConsumerFactory<String, TaskUpdateDto> consumerListenerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperty.getBootstrapServers());
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperty.getConsumer().getGroupId());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TaskDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperty.getConsumer().getAutoOffsetReset());
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskUpdateDto> taskKafkaListenerContainerFactory(
            @Qualifier("consumerListenerFactory") final ConsumerFactory<String, TaskUpdateDto> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, TaskUpdateDto> kafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, kafkaListenerContainerFactory);
        return kafkaListenerContainerFactory;
    }

    private <T> void factoryBuilder(ConsumerFactory<String, T> consumerFactory,
                                    ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory) {

        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        kafkaListenerContainerFactory.setBatchListener(true);
        kafkaListenerContainerFactory.setConcurrency(1);
        kafkaListenerContainerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        kafkaListenerContainerFactory.getContainerProperties().setPollTimeout(5000);
        kafkaListenerContainerFactory.getContainerProperties().setMicrometerEnabled(true);
        kafkaListenerContainerFactory.setCommonErrorHandler(errorHandler());
    }

    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(1000, 3));
        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);
        errorHandler.setRetryListeners(((record, ex, deliveryAttempt) ->
                log.error("Error in processing record: {}, Exception: {}, Delivery Attempt: {}",
                        record, ex.getMessage(), deliveryAttempt)));
        return errorHandler;
    }
}
