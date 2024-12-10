package com.example.demo.kafka;

import com.example.demo.model.dto.TaskUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

@Slf4j
public class TaskDeserializer implements Deserializer<TaskUpdateDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TaskUpdateDto deserialize(final String topic, final byte[] data) {
        try {
            log.debug("From {} received TaskDto: {}", topic, DatatypeConverter.printHexBinary(data));
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), TaskUpdateDto.class);
        } catch (final Exception e) {
            log.warn("Deserialize TaskUpdateDto error: from {} received: {}", topic, new String(data, StandardCharsets.UTF_8), e);
            throw new IllegalArgumentException(e);
        }
    }
}
