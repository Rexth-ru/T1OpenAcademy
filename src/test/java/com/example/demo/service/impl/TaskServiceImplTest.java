package com.example.demo.service.impl;

import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.model.Task;
import com.example.demo.model.dto.TaskDto;
import com.example.demo.model.dto.TaskResponseDto;
import com.example.demo.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;

    private Task task2;

    private TaskResponseDto taskResponseDto2;

    private TaskResponseDto taskResponseDto;

    private TaskDto taskDto;
    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setUserId("123");
        task.setStatus("PENDING");

        taskDto = new TaskDto("Test Task", "Test Description", "PENDING", "123");

        taskResponseDto = new TaskResponseDto("Test Task", "Test Description", "PENDING");

        task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setUserId("456");
        task2.setStatus("COMPLETED");

        taskResponseDto2 = new TaskResponseDto("Task 2", "Description 2", "COMPLETED");
    }

    @AfterEach
    void tearDown() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("Тест успешного получения задачи по id")
    void getTaskByIdSuccess() {

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.mapToDto(task)).thenReturn(taskResponseDto);

        TaskResponseDto result = taskService.getTaskById(1L);
        assertNotNull(result);
        assertEquals("Test Task", result.title());
    }

    @Test
    @DisplayName("Тест получения задачи по id с ошибкой")
    void testGetTaskByIdWithException() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(999L));
    }

    @Test
    @DisplayName("Тест получения всех задач")
    void getAllTasks() {

        when(taskRepository.findAll()).thenReturn(List.of(task, task2));
        when(taskMapper.mapToDto(task)).thenReturn(taskResponseDto);
        when(taskMapper.mapToDto(task2)).thenReturn(taskResponseDto2);

        List<TaskResponseDto> result = taskService.getAllTasks();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Тест успешного удаления задачи по id")
    void deleteTaskById() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTaskById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Тест удаления задачи по id с ошибкой")
    void deleteTaskByIdWithException() {
        when(taskRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> taskService.deleteTaskById(999L));
    }

    @Test
    @DisplayName("Тест сохранения задачи")
    void saveTask() {
        when(taskMapper.mapToTask(taskDto)).thenReturn(task);

        taskService.saveTask(taskDto);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Тест успешного обновления задачи")
    void updateTaskSuccess() {

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task2);
        when(taskMapper.mapToDto(any(Task.class))).thenReturn(taskResponseDto2);

        TaskResponseDto result = taskService.updateTask(taskDto, 1L);

        assertNotNull(result);
        assertEquals("Task 2", result.title());

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskMapper, times(1)).mapToDto(any(Task.class));
    }

    @Test
    @DisplayName("Тест обновления задачи с ошибкой")
    void testUpdateTaskWithException() {
        TaskDto taskDto = new TaskDto("New Task", "New Description", "COMPLETED", "123");

        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(taskDto, 999L));

        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).mapToDto(any(Task.class));
    }
}