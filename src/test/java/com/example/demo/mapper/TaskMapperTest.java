package com.example.demo.mapper;

import com.example.demo.model.Task;
import com.example.demo.model.dto.TaskDto;
import com.example.demo.model.dto.TaskResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private TaskMapper taskMapper;
    private Task task;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        taskMapper = Mappers.getMapper(TaskMapper.class);
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setUserId("123");
        task.setStatus("PENDING");
        taskDto = new TaskDto("New Task", "New Description", "PENDING", "123");
    }

    @Test
    @DisplayName("Тест маппинга сущности в dto")
    void testMapToDto() {

        TaskResponseDto taskResponseDto = taskMapper.mapToDto(task);

        assertNotNull(taskResponseDto);
        assertEquals("Test Task", taskResponseDto.title());
        assertEquals("Test Description", taskResponseDto.description());
        assertEquals("PENDING", taskResponseDto.status());
    }

    @Test
    @DisplayName("Тест маппинга dto в сущность")
    void testMapToTask() {

        task = taskMapper.mapToTask(taskDto);

        assertNotNull(task);
        assertEquals("New Task", task.getTitle());
        assertEquals("New Description", task.getDescription());
        assertEquals("123", task.getUserId());
        assertEquals("PENDING", task.getStatus());
    }
}