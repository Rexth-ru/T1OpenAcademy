package com.example.demo.kafka;

import com.example.demo.model.dto.TaskUpdateDto;
import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskUpdateKafkaListener {

    private final NotificationService notificationService;

    @KafkaListener(id = "${spring.kafka.consumer.group-id}",
            containerFactory = "taskKafkaListenerContainerFactory", topics = "${spring.kafka.topic-name}")
    public void taskUpdateStatusKafkaListener(@Payload final TaskUpdateDto taskDto, final Acknowledgment acknowledgment) {
        try {
            notificationService.sendEmail(taskDto);
        } catch (Exception e) {
            log.error("An unexpected error occurred in taskUpdateStatusKafkaListener: {}", e.getMessage());
        } finally {
            acknowledgment.acknowledge();
        }
    }
}
