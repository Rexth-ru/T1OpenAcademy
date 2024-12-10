package com.example.demo.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "spring.kafka")
@ToString
public class KafkaProp {
    private String bootstrapServers;
    private String topicName;
    private Consumer consumer;
    private Producer producer;

    @Getter
    @Setter
    @ToString
    public static class Consumer {
        private String groupId;
        private String autoOffsetReset;
        private String keyDeserializer;
        private String valueDeserializer;
    }

    @Getter
    @Setter
    @ToString
    public static class Producer {
        private String keySerializer;
        private String valueSerializer;
    }
}
