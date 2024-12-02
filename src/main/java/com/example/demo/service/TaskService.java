package com.example.demo.service;

import com.example.demo.model.dto.TaskResponseDto;
import com.example.demo.model.dto.TaskRequestDto;

import java.util.List;

public interface TaskService {

    TaskResponseDto getTaskById(Long id);

    List<TaskResponseDto> getAllTasks();

    void deleteTaskById(Long id);

    void saveTask(TaskRequestDto taskDto);

    TaskResponseDto updateTask(TaskRequestDto dto, Long id);
}
