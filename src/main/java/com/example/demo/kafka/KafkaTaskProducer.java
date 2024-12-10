package com.example.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTaskProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTo(String topic, Object o) {
        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();

        kafkaTemplate.send(topic, o).whenComplete((result, ex) -> {
            if (ex != null) {
                future.completeExceptionally(ex);

            } else {
                future.complete(result);
            }
        });

        future.thenAccept(result -> log.info("Sent message: {} with offset: {}", result.getProducerRecord().value(), result.getRecordMetadata().offset())).exceptionally(ex -> {
            log.error("Failed to send message: {}", ex.getMessage());
            return null;
        });
    }
}
